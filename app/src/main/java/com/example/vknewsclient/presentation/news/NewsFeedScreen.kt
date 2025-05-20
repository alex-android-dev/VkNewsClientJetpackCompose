package com.example.vknewsclient.presentation.news

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.presentation.getApplicationComponent
import com.example.vknewsclient.presentation.main.VK_TITLE_SCAFFOLD_STR
import com.example.vknewsclient.presentation.main.VkTopAppBar
import com.example.vknewsclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    backToAuthorize: @Composable () -> Unit,
) {

    val component = getApplicationComponent()

    Log.d("RECOMPOSITION_TAG", "NewsFeedScreen getApplicationComponent")


    val viewModel: NewsFeedViewModel = viewModel(
        factory = component.getViewModelFactory()
    )

    val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)
    Log.d("NewsFeedScreen", "state: ${screenState.value}")

    NewsFeedScreenContent(
        screenState = screenState.value,
        viewModel = viewModel,
        paddingValues = paddingValues,
        onCommentClickListener = onCommentClickListener,
        backToAuthorize = { backToAuthorize() },
    )

}

/**
 * Делаем отдельную компоузбл функцию, чтобы у нас не пересоздавались объекты в основной функции
 * Передаём именно стейт, чтобы функция реагировала на изменения screenState и происходила рекомпозиция при изменениях
 */
@Composable
private fun NewsFeedScreenContent(
    screenState: NewsFeedScreenState,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    backToAuthorize: @Composable () -> Unit,
) {
    when (screenState) {

        is NewsFeedScreenState.Posts -> VkNewsFeedScreen(
            posts = screenState.posts,
            viewModel = viewModel,
            paddingValues = paddingValues,
            onCommentClickListener = { feedPost ->
                onCommentClickListener(feedPost)
            },
            nextDataIsLoading = screenState.nextDataIsLoading
        )

        is NewsFeedScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }

        is NewsFeedScreenState.Error -> {
            backToAuthorize()
        }

        is NewsFeedScreenState.Initial -> {}
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun VkNewsFeedScreen(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean,
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
        LazyColumnFeedPosts(
            posts = posts,
            viewModel = viewModel,
            paddingValues = paddingValues,
            onCommentClickListener = { feedPost ->
                onCommentClickListener(feedPost)
            },
            nextDataIsLoading = nextDataIsLoading
        )
    }
}

@Composable
private fun LazyColumnFeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean,
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
                    onLikeClickListener = { _ ->
                        viewModel.changeLikeStatus(feedPost)
                    },
                    onCommentClickListener = {
                        onCommentClickListener(feedPost)
                    },
                )
            }
        }

        item {
            Log.d("NewsFeedScreen", "nextDataIsLoading: $nextDataIsLoading")
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkBlue)
                }
            } else {
                SideEffect {
                    // TODO тут баг. Не работает загрузка старых постов
                    viewModel.loadNextRecommendations()
                }
            }
        }

    }
}