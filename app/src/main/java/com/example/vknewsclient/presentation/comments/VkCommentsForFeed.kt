package com.example.vknewsclient.presentation.comments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.vknewsclient.R
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.presentation.MyApplication
import com.example.vknewsclient.presentation.ViewModelFactory
import com.example.vknewsclient.presentation.getApplicationComponent
import com.example.vknewsclient.presentation.main.VkTopAppBar
import com.example.vknewsclient.ui.theme.DarkBlue

@Composable
fun VkCommentsScreen(
    paddingValues: PaddingValues,
    onBackPressed: () -> Unit,
    feedPost: FeedPost,
) {

    /**
     * Будет создана реализации CommentsScreen Component
     * Компонент будет содержать необходимую зависимость для CommentsViewModel
     * ViewModel будет добавлена в мапу с соотв. ключом
     * Затем мы получаем ViewModelFactory в которой будет лежать необходимая реализация ViewModel (с фидпостом)
     */
    val appComponent = getApplicationComponent().getCommentsScreenComponentFactory().create(feedPost)

    val viewModel: CommentsViewModel = viewModel(
        factory = appComponent.getViewModelFactory()
    )

    val screenState = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            VkTopAppBar(
                title = stringResource(R.string.comments_title),
                Icons.AutoMirrored.Filled.ArrowBack,
                onIconClickListener = { onBackPressed() }
            )
        },
    ) { paddingValues ->

        when (val currentState = screenState.value) {
            is CommentsScreenState.Comments -> LazyColumnCommentsForFeedPost(
                paddingValues,
                currentState.comments
            )

            is CommentsScreenState.Loading -> {
                Log.d("VkCommentsScreen", "Loading")
                ShowLoading()
            }

            is CommentsScreenState.Initial -> {}
        }

    }
}


@Composable
private fun LazyColumnCommentsForFeedPost(
    paddingValues: PaddingValues,
    postCommentsList: List<PostComment>,
) {

    val lazyStateList = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = lazyStateList,
    ) {
        items(
            items = postCommentsList,
            key = { it.id }
        ) { comment ->
            CommentItem(comment)

        }
    }
}

@Composable
private fun CommentItem(postComment: PostComment) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {

        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = postComment.avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopCenter
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(postComment.authorName)
            Text(postComment.commentText)
            Text(postComment.publicationDate)
        }
    }
}

@Composable
private fun ShowLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = DarkBlue)
    }
}