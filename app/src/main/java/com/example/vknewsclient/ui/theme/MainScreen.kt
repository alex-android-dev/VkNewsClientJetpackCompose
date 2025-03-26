package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.navigation.AppNavGraph
import com.example.vknewsclient.navigation.rememberNavigationState


@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val commentsToPost: MutableState<FeedPost?> = remember {
        mutableStateOf(null)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            VkNavigationBar(navigationState)
        },
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                if (commentsToPost.value == null) {
                    HomeScreen(
                        paddingValues,
                        onCommentClickListener = {
                            commentsToPost.value = it
                        }
                    )
                } else {
                    VkCommentsScreen(
                        onBackPressed = { commentsToPost.value = null },
                        feedPost = commentsToPost.value!! // TODO временное решение
                    )
                }
            },
            favouriteScreen = { TextCounter("favouriteScreen", paddingValues) },
            profileScreen = { TextCounter("profileScreen", paddingValues) },
        )
    }
}