package com.example.vknewsclient.domain

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()
    // Необходим, чтобы отрисовать какой-то прогресс при старте загрузки

//    data class Posts(val posts: List<FeedPost>) : CommentsScreenState()
    data class Comments(val post: FeedPost, val comments: List<PostComment>) : CommentsScreenState()
}