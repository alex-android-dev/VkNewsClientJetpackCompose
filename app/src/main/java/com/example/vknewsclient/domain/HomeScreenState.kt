package com.example.vknewsclient.domain

sealed class HomeScreenState {
    object Initial : HomeScreenState()
    data class Posts(val posts: List<FeedPost>) : HomeScreenState()
    data class Comments(val post: FeedPost, val comments: List<PostComment>) : HomeScreenState()


}