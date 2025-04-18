package com.example.vknewsclient.presentation.comments

import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()
    // Необходим, чтобы отрисовать какой-то прогресс при старте загрузки

    object Loading : CommentsScreenState()

    data class Comments(val post: FeedPost, val comments: List<PostComment>) : CommentsScreenState()
}