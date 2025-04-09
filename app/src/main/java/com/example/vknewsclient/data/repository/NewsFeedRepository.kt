package com.example.vknewsclient.data.repository

import android.util.Log
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.FeedPost
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail

class NewsFeedRepository {

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()
    val token = VKID.Companion.instance.accessToken?.token
        ?: throw IllegalStateException("token is null")

    suspend fun loadRecommendation(): List<FeedPost> {
        val response: NewsFeedResponseDto = apiService.loadRecommendations(token)
        return mapper.mapResponseToPosts(response)
    }

    suspend fun addLike(feedPost: FeedPost) {
        apiService.addLike(
            token = token,
            ownerId = -feedPost.communityId,
            postId = feedPost.id
        )
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

