package com.example.vknewsclient.data.model.NewsFeedModelDto

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("sizes") val photoUrls : List<PhotoUrlDto>,
)
