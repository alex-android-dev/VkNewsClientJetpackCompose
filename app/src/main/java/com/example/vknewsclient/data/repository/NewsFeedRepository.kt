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

class NewsFeedRepository {

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()
    private val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts
        get() = _feedPosts.toList()


    suspend fun loadRecommendation(): List<FeedPost> {
        val response: NewsFeedResponseDto = apiService.loadRecommendations(token)
        val posts = mapper.mapResponseToPosts(response)
        _feedPosts.addAll(posts)
        return posts
    }

    suspend fun addLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId
        val response = apiService.addLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        changeLikeInFeedPosts(response, feedPost)
    }

    suspend fun deleteLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val response = apiService.deleteLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id,
        )

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

        _feedPosts[postIndex] = newPost
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

