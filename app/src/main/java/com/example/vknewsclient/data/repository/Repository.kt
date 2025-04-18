package com.example.vknewsclient.data.repository

import android.util.Log
import com.example.vknewsclient.data.mapper.Mapper
import com.example.vknewsclient.data.model.CommentsDto.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedModelDto.LikesCountResponse
import com.example.vknewsclient.data.model.NewsFeedModelDto.NewsFeedResponseDto
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.extensions.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class Repository {

    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()
    private val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPostList
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val scope = CoroutineScope(Dispatchers.Default)

    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        /* Мы эмитим объект типа юнит, чтобы дать другому коллекту сигнал продолжить работу
        Эмит прилетает -> стартует загрузка данных
         */

        nextDataNeededEvents.collect() {
            val startFrom = nextFrom
            // Делаем так, чтобы проверка if - else отрабатывала корректно

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

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    // Чтобы последний эмит был учтен. Иначе поток его не увидит при начальной подписке


    val recommendations: StateFlow<List<FeedPost>> =
        loadedListFlow
            .mergeWith(refreshedListFlow)
            .stateIn(scope = scope, started = SharingStarted.Lazily, initialValue = feedPostList)


    suspend fun loadNextData() {
        delay(2000)
        nextDataNeededEvents.emit(Unit)
    }

    suspend fun loadCommentsToPost(feedPost: FeedPost): List<PostComment> {
        val ownerId = -feedPost.communityId
        // TODO временная затычка. нужно подумать как изменить, чтобы не втыкать её везде

        val response: CommentsResponseDto = apiService.getCommentsToPost(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        return mapper.mapCommentsResponseDtoToComments(response)
    }

    suspend fun removePost(feedPost: FeedPost) {
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
        refreshedListFlow.emit(feedPostList)
    }

    suspend fun addLike(feedPost: FeedPost) {
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

    suspend fun deleteLike(feedPost: FeedPost) {
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
        refreshedListFlow.emit(feedPostList)
    }


//    suspend fun refreshToken() {
//        Log.d("NewsFeedRepository", "refreshToken()")
//        VKID.instance.refreshToken(
//            callback = object : VKIDRefreshTokenCallback {
//                override fun onSuccess(token: AccessToken) {
//                }
//
//                override fun onFail(fail: VKIDRefreshTokenFail) {
//                    Log.d("NewsFeedRepository", fail.description)
//                }
//            }
//        )
//    } // TODO Данный метод не работает. Нужно понять как корректно рефрешить токен
}

