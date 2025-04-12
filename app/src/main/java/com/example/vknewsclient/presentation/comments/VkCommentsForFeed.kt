package com.example.vknewsclient.presentation.comments

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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.vknewsclient.R
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.presentation.main.VkTopAppBar
import com.example.vknewsclient.ui.theme.DarkBlue
import java.nio.file.WatchEvent

const val COMMENTS_FOR_POST_TITLE_SCAFFOLD_STR = "Comments for Post"

@Composable
fun VkCommentsScreen(
    paddingValues: PaddingValues,
    onBackPressed: () -> Unit,
    feedPost: FeedPost,
) {
    val viewModel: CommentsViewModel = viewModel(
        factory = CommentsViewModelFactory(feedPost),
    )

    val screenState = viewModel.screenState.observeAsState(CommentsScreenState.Initial)
    val currentState = screenState.value

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

        when (currentState) {
            is CommentsScreenState.Comments -> {
                LazyColumnCommentsForFeedPost(
                    paddingValues,
                    currentState.comments,
                )
            }

            CommentsScreenState.Loading -> {
                ShowLoading()
            }

            CommentsScreenState.Initial -> {}
        }

    }
}

@Composable
private fun ShowLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = DarkBlue)
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
//        verticalAlignment = Alignment.CenterVertically,
    ) {

//        AsyncImage(
//            model = postComment.avatarUrl,
//            tint = MaterialTheme.colorScheme.onBackground,
//            modifier = Modifier,
//            contentDescription = null
//        )

        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = postComment.avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopCenter
        ) // TODO проверить корректность

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

//@Preview
//@Composable
//fun PreviewPostComment() {
//
//    val postCommentsList = mutableListOf<PostComment>().apply {
//        repeat(20) {
//            add(
//                PostComment(
//                    id = it,
//                    authorName = "author number $it",
//                )
//            )
//        }
//    }.toList()
//
//    val feedPost = FeedPost(
//        id = 85
//    )
//
//
//    VkCommentsForFeedPosts(postCommentsList, feedPost)
//
//}


// todo: остался баг последний комментарий почему-то не отображается