package com.example.vknewsclient.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vknewsclient.navigation.AppNavGraph
import com.example.vknewsclient.navigation.VkBottomNavigationBar
import com.example.vknewsclient.navigation.rememberNavigationState
import com.example.vknewsclient.presentation.comments.VkCommentsScreen
import com.example.vknewsclient.presentation.news.NewsFeedScreen


@Composable
fun VkNewsMainScreen(
    backToAuthorize: () -> Unit,
) {
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
                NewsFeedScreen(
                    paddingValues,
                    onCommentClickListener = { feedPost ->
                        navigationState.navigateToComments(feedPost)
                    },
                    backToAuthorize = { backToAuthorize() }
                )
            },
            commentsScreenContent = { feedPost ->
                VkCommentsScreen(
                    paddingValues,
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