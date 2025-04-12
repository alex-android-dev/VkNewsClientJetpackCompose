package com.example.vknewsclient.data.model.CommentsDto

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val comments: List<CommentsDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
)
