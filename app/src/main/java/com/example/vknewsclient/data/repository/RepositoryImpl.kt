package com.example.vknewsclient.data.repository

import android.util.Log
import com.example.vknewsclient.data.mapper.Mapper
import com.example.vknewsclient.data.model.CommentsDto.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedModelDto.LikesCountResponse
import com.example.vknewsclient.data.model.NewsFeedModelDto.NewsFeedResponseDto
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.NewsFeedResult
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import com.example.vknewsclient.domain.repository.Repository
import com.example.vknewsclient.extensions.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class RepositoryImpl : Repository {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    private val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPostList
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val scope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<NewsFeedResult>()

    // Отвечает за загрузку новых данных
    private val loadedListFlow = getLoadedListFlow()
        /** Маппим к классу Result и передаем наружу **/
        .map { NewsFeedResult.Success(it) as NewsFeedResult }
        /** Данный блок позволяет повторять запрос с определенной задержкой (чтобы не ддосить сервер) **/
        .retry(RETRY_TRIES) {
            delay(RETRY_TIMEOUT_MILLIS)
            true
        }
        /** Происходит после retry **/
        /** Холодный флоу больше не эмитит данные **/
        .catch { emit(NewsFeedResult.Error) }

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    // Чтобы последний эмит был учтен. Иначе поток его не увидит при начальной подписке
    private val recommendationPosts: StateFlow<NewsFeedResult> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = NewsFeedResult.Success(feedPostList)
        )

    private val commentsToPost = MutableStateFlow<List<PostComment>>(listOf())

    override fun getRecommendations(): StateFlow<NewsFeedResult> = recommendationPosts

    override fun getCommentsToPost(): StateFlow<List<PostComment>> = commentsToPost

    // TODO тут достаточно использовать холодные флоу. Переделать
    override suspend fun loadCommentsToPost(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId
        // TODO временная затычка. нужно подумать как изменить, чтобы не втыкать её везде
        val response: CommentsResponseDto = apiService.getCommentsToPost(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        val comments = mapper.mapCommentsResponseDtoToComments(response)
        Log.d("Repository", "Comments: $comments")

        commentsToPost.emit(comments)
    }

    override suspend fun loadNextData() {
        delay(2000)
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun removePost(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId
        // TODO временная затычка. нужно подумать как изменить, чтобы не втыкать её везде

        val postId = feedPost.id

        apiService.removeItem(
            token = token,
            ownerId = ownerId,
            postId = postId,
        )

        val feedPostItem = _feedPosts.find { it.id == feedPost.id }
        _feedPosts.remove(feedPostItem)

        refreshedListFlow.emit(NewsFeedResult.Success(feedPostList))
    }

    override suspend fun addLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId
        // TODO временная затычка. нужно подумать как изменить, чтобы не втыкать её везде

        val response = apiService.addLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        if (response != null) {
            Log.d("NewsFeedRepository", "response: $response")
            Log.d("NewsFeedRepository", "feedpost: $feedPost")

            changeLikeInFeedPosts(response, feedPost)
        }

    }

    override suspend fun deleteLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId
        // TODO временная затычка. нужно подумать как изменить, чтобы не втыкать её везде

        val response = apiService.deleteLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id,
        )

        if (response != null)
            changeLikeInFeedPosts(response, feedPost)

    }


    /**
     * Мы эмитим объект типа юнит, чтобы дать другому коллекту сигнал продолжить работу
     * Эмит прилетает -> стартует загрузка данных **/
    /**
     * Мы эмитим объект типа юнит, чтобы дать другому коллекту сигнал продолжить работу
     * Эмит прилетает -> стартует загрузка данных
     * **/
    private fun getLoadedListFlow(): Flow<List<FeedPost>> = flow {
        nextDataNeededEvents.emit(Unit)

        nextDataNeededEvents.collect() {
            val startFrom = nextFrom
            /** Делаем так, чтобы проверка if - else отрабатывала корректно **/

            if (startFrom == null && feedPostList.isNotEmpty()) {
                emit(feedPostList)
                return@collect
            }

            val response: NewsFeedResponseDto =
                if (startFrom == null) {
                    apiService.loadRecommendations(token)
                } else {
                    apiService.loadRecommendations(token, startFrom)
                }

            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapNewsFeedResponseToPosts(response)
            _feedPosts.addAll(posts)
            emit(feedPostList)
        }
    }

    private suspend fun changeLikeInFeedPosts(
        response: LikesCountResponse,
        feedPost: FeedPost,
    ) {
        val newLikesCount = response.likesCountDto.count

        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(
            statistics = newStatistics, isLiked = !feedPost.isLiked
        )

        val postIndex = _feedPosts.indexOf(feedPost)

        if (postIndex != -1) _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(NewsFeedResult.Success(feedPostList))
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS: Long = 3000L
        private const val RETRY_TRIES: Long = 3L
    }

}

