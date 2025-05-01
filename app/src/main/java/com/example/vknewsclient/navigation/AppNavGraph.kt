package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vknewsclient.domain.entity.FeedPost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    favouriteScreen: @Composable () -> Unit,
    profileScreen: @Composable () -> Unit,
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route, // Указывается какой экран будет открыт первым
    ) {
        // Билдер. Здесь будем строить весь граф навигации
        // Добавляем сюда различные направления / переходы, которые будут в этом графе

        homeScreenNavGraph(
            newsFeedScreenContent = newsFeedScreenContent,
            commentsScreenContent = commentsScreenContent,
        )

        // Если мы хотим вызвать еще какой-то экран, то также передаем функцию composable
        composable(route = Screen.Favorite.route) { favouriteScreen() }
        composable(route = Screen.Profile.route) { profileScreen() }

    }
}