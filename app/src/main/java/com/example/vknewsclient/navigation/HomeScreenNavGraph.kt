package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.vknewsclient.domain.FeedPost
import com.google.gson.Gson

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
                    name = Screen.KEY_FEED_POST, // ключ для аргумента
                    builder = { // билдер аргумента
                        type = NavType.StringType // Указываем, что тип String
                    }
                ),
            )
        ) { navBackStackEntry -> // comments/{feed_post} JSON format
            val feedPostJson =
                navBackStackEntry.arguments?.getString(Screen.KEY_FEED_POST) ?: ""
            val feedPost = Gson().fromJson(feedPostJson, FeedPost::class.java)
            commentsScreenContent(feedPost)
        }
    }

}