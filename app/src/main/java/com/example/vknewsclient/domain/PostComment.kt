package com.example.vknewsclient.domain

import com.example.vknewsclient.R

data class PostComment(
    val id: Long,
    val authorName: String = "Author",
    val avatarUrl : String,
    val commentText: String = "Long comment text",
    val publicationDate: String = "04:15",
)