package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

// Чтобы мы из JSON вытащили нужные нам столбцы
data class PostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("attachments") val posts: List<AttachmentDto>?,
)