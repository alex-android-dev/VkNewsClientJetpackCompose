package com.example.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.vknewsclient.ui.theme.PostCard
import com.example.vknewsclient.ui.theme.VkNavigationBar
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.example.vknewsclient.ui.theme.VkTopAppBar

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme() {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val feedPosts = viewModel.feedPostsLiveData.observeAsState(listOf())
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            VkTopAppBar()
        },
        bottomBar = {
            VkNavigationBar()
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            VkNewsFeedLazyColumn(viewModel)
        }
    }
}

@Composable
private fun VkNewsFeedLazyColumn(viewModel: MainViewModel) {

    val feedPosts = viewModel.feedPostsLiveData.observeAsState(listOf())
    Log.d("MainActivity", "feedposts state: ${feedPosts.value.size}")
    val lazyStateList = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background,
        ),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = lazyStateList,
    ) {

        items(
            items = feedPosts.value,
            key = { it.id }
        ) { feedPost ->

            val dismissThresholds = with(receiver = LocalDensity.current) {
                LocalConfiguration.current.screenWidthDp.dp.toPx() * 0.30f
            }

            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { dismissThresholds },
                confirmValueChange = { value ->
                    Log.d("MainActivity", "Swipe to delete")
                    val isDismissed = value == SwipeToDismissBoxValue.EndToStart
                    Log.d("MainActivity", "feedpost status delete: $feedPost")
                    if (isDismissed) viewModel.removePost(feedPost)
                    true
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = dismissState,
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
                        Log.d("MainActivity", "feedpost status onView: $feedPost")
                    },
                    onCommentClickListener = {
                        viewModel.updateStatisticCard(feedPost, it)
                    },
                )
            }
        }
    }
}