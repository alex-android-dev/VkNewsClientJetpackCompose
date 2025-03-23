package com.example.vknewsclient.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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

@Composable
fun HomeScreen(viewModel: MainViewModel, paddingValues: PaddingValues) {

    val feedPosts = viewModel.feedPostsLiveData.observeAsState(listOf())
    Log.d("MainActivity", "feedposts state: ${feedPosts.value.size}")
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
            items = feedPosts.value,
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
                        viewModel.updateStatisticCard(feedPost, it)
                    },
                )
            }
        }
    }
}