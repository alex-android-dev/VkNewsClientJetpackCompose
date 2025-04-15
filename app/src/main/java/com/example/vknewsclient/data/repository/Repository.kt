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
import com.vk.id.VKID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository {

    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()
    private val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    fun loadRecommendation(): Flow<List<FeedPost>> = flow {
        val startFrom = nextFrom
        // Делаем так, чтобы проверка if - else отрабатывала корректно

        if (startFrom == null && feedPosts.isNotEmpty()) {
            emit(feedPosts)
            return@flow
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
        emit(feedPosts)
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

    private fun changeLikeInFeedPosts(
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

