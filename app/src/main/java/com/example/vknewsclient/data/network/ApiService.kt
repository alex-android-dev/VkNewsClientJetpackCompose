package com.example.vknewsclient.data.network

import com.example.vknewsclient.data.model.CommentsDto.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedModelDto.LikesCountResponse
import com.example.vknewsclient.data.model.NewsFeedModelDto.NewsFeedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

const val APP_API_VERSION = "v=5.199"
const val TYPE_POST = "post"
const val TYPE_WALL = "wall"
const val STR_TYPE = "type"
const val GET_RECOMMENDED = "newsfeed.getRecommended"
const val GET_COMMENTS = "wall.getComments"

interface ApiService {

    @GET("$GET_RECOMMENDED?$APP_API_VERSION")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
    ): NewsFeedResponseDto

    @GET("$GET_RECOMMENDED?$APP_API_VERSION")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String,
    ): NewsFeedResponseDto

    @GET("newsfeed.ignoreItem?$APP_API_VERSION&type=$TYPE_WALL")
    suspend fun removeItem(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    )

    @GET("likes.add?$APP_API_VERSION&$STR_TYPE=$TYPE_POST")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponse?

    @GET("likes.delete?$APP_API_VERSION&$STR_TYPE=$TYPE_POST")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponse?

    // TODO подумать как доставать больше комментариев
    @GET("$GET_COMMENTS?$APP_API_VERSION&extended=1&count=100&fields=photo_100")
    suspend fun getCommentsToPost(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
    ): CommentsResponseDto


}