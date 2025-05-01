package com.example.vknewsclient.domain.repository

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.NewsFeedResult
import com.example.vknewsclient.domain.entity.PostComment
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun getRecommendations(): StateFlow<NewsFeedResult>

    fun getCommentsToPost(): StateFlow<List<PostComment>>

    suspend fun loadCommentsToPost(feedPost: FeedPost)

    suspend fun loadNextData()

    suspend fun removePost(feedPost: FeedPost)

    suspend fun addLike(feedPost: FeedPost)

    suspend fun deleteLike(feedPost: FeedPost)

}