package com.example.vknewsclient.domain

import com.example.vknewsclient.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val authorAvatarId : Int = R.drawable.ic_account_avatar,
    val commentText: String = "Long comment text",
    val publicationDate: String = "04:15",
)