package com.example.vknewsclient.data.model.NewsFeedModelDto

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseDto(
    @SerializedName("response") val newsFeedContent: NewsFeedContentDto,
)
