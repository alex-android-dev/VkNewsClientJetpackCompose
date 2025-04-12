package com.example.vknewsclient.data.model.CommentsDto

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val commentsContent: CommentsContentDto,
)
