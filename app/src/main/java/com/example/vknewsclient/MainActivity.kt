package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.domain.FeedPost
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

    val lazyStateList = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background,
        ),
        state = lazyStateList,
    ) {

        items(
            items = feedPosts.value,
            key = { it.id }
        ) { feedPost ->
            PostCard(
                modifier = Modifier.padding(8.dp),
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

// Отображение Lazy Column
// Клики работают на отдельный пост
// todo Удаление при помощи свайпа