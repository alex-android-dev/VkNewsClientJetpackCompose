package com.example.vknewsclient.presentation.comments

import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()
    // Необходим, чтобы отрисовать какой-то прогресс при старте загрузки

//    data class Posts(val posts: List<FeedPost>) : CommentsScreenState()
    data class Comments(val post: FeedPost, val comments: List<PostComment>) : CommentsScreenState()
}