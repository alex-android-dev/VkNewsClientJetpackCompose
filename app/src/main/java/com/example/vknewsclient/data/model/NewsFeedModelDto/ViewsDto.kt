package com.example.vknewsclient.data.model.NewsFeedModelDto

import com.google.gson.annotations.SerializedName

data class ViewsDto(
    @SerializedName("count") val count: Int
) {
}