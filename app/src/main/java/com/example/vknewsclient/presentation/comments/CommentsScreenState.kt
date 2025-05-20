package com.example.vknewsclient.presentation.comments

import com.example.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {
    data object Initial : CommentsScreenState()

    /** Необходим, чтобы отрисовать какой-то прогресс при старте загрузки **/
    data object Loading : CommentsScreenState()

    data class Comments(val comments: List<PostComment>) : CommentsScreenState()
}