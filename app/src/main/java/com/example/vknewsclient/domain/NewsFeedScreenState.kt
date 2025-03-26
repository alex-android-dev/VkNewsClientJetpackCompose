package com.example.vknewsclient.domain

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
//    data class Comments(val post: FeedPost, val comments: List<PostComment>) : HomeScreenState()
}