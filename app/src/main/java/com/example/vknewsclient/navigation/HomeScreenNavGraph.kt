package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable () -> Unit,
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

        composable(route = Screen.Comments.route) {
            commentsScreenContent()
        }
    }

}