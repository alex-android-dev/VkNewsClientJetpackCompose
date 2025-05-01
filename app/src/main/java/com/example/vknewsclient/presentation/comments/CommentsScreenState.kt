package com.example.vknewsclient.presentation.comments

import com.example.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {
    object Initial : CommentsScreenState()

    /** Необходим, чтобы отрисовать какой-то прогресс при старте загрузки **/
    object Loading : CommentsScreenState()

    data class Comments(val comments: List<PostComment>) : CommentsScreenState()
}