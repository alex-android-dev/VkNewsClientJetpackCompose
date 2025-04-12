package com.example.vknewsclient.data.repository

import android.util.Log
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.model.LikesCountResponse
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.vk.id.VKID
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NewsFeedRepository {

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()
    private val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts
        get() = _feedPosts.toList()

    private var nextFrom: String? = null
    val mutex = Mutex()

    suspend fun loadRecommendation(): List<FeedPost> {
        val startFrom = nextFrom
        // Делаем так, чтобы проверка if - else отрабатывала корректно

        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response: NewsFeedResponseDto =
            if (startFrom == null) {
                apiService.loadRecommendations(token)
            } else {
                apiService.loadRecommendations(token, startFrom)
            }

        nextFrom = response.newsFeedContent.nextFrom
        val posts = mapper.mapResponseToPosts(response)
        _feedPosts.addAll(posts)
        return feedPosts
    }

    suspend fun removePost(feedPost: FeedPost) {
        val ownerId = -(feedPost.communityId)
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

