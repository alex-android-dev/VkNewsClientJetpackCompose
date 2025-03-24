package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    favouriteScreen: @Composable () -> Unit,
    profileScreen: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.NewsFeed.route, // Указывается какой экран будет открыт первым
    ) {
        // Билдер. Здесь будем строить весь граф навигации
        // Добавляем сюда различные направления / переходы, которые будут в этом графе

        // Добавляет в граф новое направление
        composable(
            route = Screen.NewsFeed.route, // Название экрана на который нужно перейти
        ) {
            // Тело контента. Тут будем работать с вьюшками
            // Воспользуемся коллбэками и получим тут Composable функции, которые будем вызывать
            homeScreenContent()
        }

        // Если мы хотим вызвать еще какой-то экран, то также передаем функцию composable
        composable(route = Screen.Favorite.route) { favouriteScreen() }
        composable(route = Screen.Profile.route) { profileScreen() }

    }
}