package com.example.vknewsclient.presentation.news

import com.example.vknewsclient.domain.entity.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    object Loading : NewsFeedScreenState()
    object Error : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}