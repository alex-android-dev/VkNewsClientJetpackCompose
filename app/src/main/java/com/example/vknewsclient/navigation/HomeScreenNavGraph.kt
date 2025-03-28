package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.vknewsclient.domain.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
) {

    navigation(
        startDestination = Screen.NewsFeed.route, // Начальный экран в графе Home
        route = Screen.Home.route, // Имя вложенного графа навигации
    ) { // Билдер для строительства графа

        // Добавляет в граф новое направление
        composable(
            route = Screen.NewsFeed.route, // Название экрана на который нужно перейти
        ) {
            // Тело контента. Тут будем работать с вьюшками
            // Воспользуемся коллбэками и получим тут Composable функции, которые будем вызывать
            newsFeedScreenContent()
        }

        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                // Позволяет передавать разные аргументы
                navArgument( // Добавление аргумента в коллекцию
                    name = Screen.KEY_FEED_POST_ID, // ключ для аргумента
                    builder = { // билдер аргумента
                        type = NavType.IntType // Указываем, что тип Int
                    }
                ),
                navArgument(
                    name = Screen.KEY_FEED_POST_TEXT,
                    builder = {
                        type = NavType.StringType
                    }
                ),
            )
        ) { navBackStackEntry -> // comments/{feed_post_id}/{feed_post_text}
            val feedPostId =
                navBackStackEntry.arguments?.getInt(Screen.KEY_FEED_POST_ID) ?: 0

            val feedPostString =
                navBackStackEntry.arguments?.getString(Screen.KEY_FEED_POST_TEXT) ?: ""

            commentsScreenContent(
                FeedPost(
                    id = feedPostId,
                    contentText = feedPostString
                )
            )
        }
    }

}