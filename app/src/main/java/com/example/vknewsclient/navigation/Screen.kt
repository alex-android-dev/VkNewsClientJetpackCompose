package com.example.vknewsclient.navigation

import android.net.Uri
import com.example.vknewsclient.domain.FeedPost
import com.google.gson.Gson

// Класс, где хранятся все экраны
// Объект содержит название экрана в виде строки
sealed class Screen(
    val route: String
) {
    object Favorite : Screen(ROUTE_FAVOURITE)
    object Profile : Screen(ROUTE_PROFILE)

    object NewsFeed : Screen(ROUTE_NEWS_FEED)

    // Вложенный экран навигации, который будет представлять Comments и NewsFeed
    object Home : Screen(ROUTE_HOME)

    object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_COMMENTS_FOR_ARGS = "comments"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            // Преобразование в JSON

            val result = "${ROUTE_COMMENTS_FOR_ARGS}/${feedPostJson.encode()}"
            return result
        }

    }

    companion object {
        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}" // Экран Лента комментариев

        const val ROUTE_HOME = "home" // Экран Лента с постами
    }

}

fun String.encode(): String = Uri.encode(this)