package com.example.vknewsclient.navigation

import android.net.Uri
import com.example.vknewsclient.domain.FeedPost

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
            val feedPostId = feedPost.id

            val feedPostText = feedPost.contentText.encode()
            // Экранирование текста (в случае если текст приходит с такой чертой "/"
            // Вызываем каждый раз в случае если передаём строку в качестве параметра

            val result = "${ROUTE_COMMENTS_FOR_ARGS}/$feedPostId/$feedPostText"
            return result
        }

    }

    companion object {
        const val KEY_FEED_POST_ID = "feed_post_id"
        const val KEY_FEED_POST_TEXT = "feed_post_text"

        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_COMMENTS =
            "comments/{$KEY_FEED_POST_ID}/{$KEY_FEED_POST_TEXT}" // Экран Лента комментариев
        // Включаем сюда ключ feed_post_id, чтобы он принимался composable функцией и мы могли достать айдишку по этому ключу


        const val ROUTE_HOME = "home" // Экран Лента с постами
    }

}

fun String.encode(): String = Uri.encode(this)