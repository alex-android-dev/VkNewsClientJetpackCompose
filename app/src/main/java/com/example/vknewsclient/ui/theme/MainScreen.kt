package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vknewsclient.navigation.AppNavGraph
import com.example.vknewsclient.navigation.rememberNavigationState


@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            VkBottomNavigationBar(navigationState)
        },
    ) { paddingValues ->

        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    paddingValues,
                    onCommentClickListener = { feedPost ->
                        navigationState.navigateToComments(feedPost)
                    }
                )
            },
            commentsScreenContent = { feedPost ->
                VkCommentsScreen(
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                        // Если пользователь кликает на кнопку назад, то закрываем экран
                    },
                    feedPost = feedPost
                )
            },
            favouriteScreen = { TextCounter("favouriteScreen", paddingValues) },
            profileScreen = { TextCounter("profileScreen", paddingValues) },
        )
    }
}