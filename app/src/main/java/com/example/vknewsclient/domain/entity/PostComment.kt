package com.example.vknewsclient.domain.entity

data class PostComment(
    val id: Long,
    val authorName: String,
    val avatarUrl : String,
    val commentText: String,
    val publicationDate: String,
)