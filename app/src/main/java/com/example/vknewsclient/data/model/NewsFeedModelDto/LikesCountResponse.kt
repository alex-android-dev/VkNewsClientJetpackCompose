package com.example.vknewsclient.data.model.NewsFeedModelDto

import com.google.gson.annotations.SerializedName

data class LikesCountResponse(
    @SerializedName("response") val likesCountDto: LikesCountDto
)
