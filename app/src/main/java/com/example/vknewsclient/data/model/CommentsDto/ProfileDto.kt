package com.example.vknewsclient.data.model.CommentsDto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("photo_100") val photoUrl: String,
)
