package com.example.vknewsclient.ui.theme

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.MainViewModel
import com.example.vknewsclient.VK_TITLE_SCAFFOLD_STR
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.HomeScreenState
import com.example.vknewsclient.navigation.AppNavGraph

@Composable
fun HomeScreen(viewModel: MainViewModel, paddingValues: PaddingValues) {

    val screenState = viewModel.screenState.observeAsState(HomeScreenState.Initial)

    when (val currentState = screenState.value) {
        is HomeScreenState.Comments -> {
            VkCommentsForFeedPosts(
                postCommentsList = currentState.comments,
                feedPost = currentState.post,
                onBackPressed = { viewModel.closeComments() },
            )

            BackHandler {
                viewModel.closeComments()
            } // При нажатии на кнопку назад
        }

        is HomeScreenState.Posts -> VkNewsFeedScreen(
            posts = currentState.posts,
            viewModel = viewModel,
            paddingValues = paddingValues,
        )

        is HomeScreenState.Initial -> {}
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun VkNewsFeedScreen(
    posts: List<FeedPost>,
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues),
        topBar = {
            VkTopAppBar(VK_TITLE_SCAFFOLD_STR, Icons.Filled.Menu, {})
        },
    ) { paddingValues ->
        LazyColumnFeedPosts(posts, viewModel, paddingValues)
    }
}

@Composable
private fun LazyColumnFeedPosts(
    posts: List<FeedPost>,
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
) {
    val lazyStateList = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = lazyStateList,
    ) {

        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->

            val dismissThresholds = with(receiver = LocalDensity.current) {
                LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.30f
            }

            val state = rememberSwipeToDismissBoxState(
                positionalThreshold = { dismissThresholds },
                confirmValueChange = { value ->
                    val isDismissed = value == SwipeToDismissBoxValue.EndToStart
                    if (isDismissed) viewModel.removePost(feedPost)
                    true
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = state,
                enableDismissFromEndToStart = true,
                enableDismissFromStartToEnd = false,
                backgroundContent = {},
            ) {
                PostCard(
                    feedPost,
                    onLikeClickListener = {
                        viewModel.updateStatisticCard(feedPost, it)
                    },
                    onShareClickListener = {
                        viewModel.updateStatisticCard(feedPost, it)
                    },
                    onViewsClickListener = {
                        viewModel.updateStatisticCard(feedPost, it)
                    },
                    onCommentClickListener = {
                        viewModel.showComments(feedPost)
                    },
                )
            }
        }
    }
}