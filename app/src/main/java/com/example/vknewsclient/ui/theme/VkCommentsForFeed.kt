package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.CommentsViewModel
import com.example.vknewsclient.CommentsViewModelFactory
import com.example.vknewsclient.domain.CommentsScreenState
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment

const val COMMENTS_FOR_POST_TITLE_SCAFFOLD_STR = "Comments for Post"

@Composable
fun VkCommentsScreen(
    onBackPressed: () -> Unit,
    feedPost: FeedPost,
) {
    val viewModel: CommentsViewModel = viewModel(
        factory = CommentsViewModelFactory(feedPost),
    )

    val screenState = viewModel.screenState.observeAsState(CommentsScreenState.Initial)
    val currentState = screenState.value

    if (currentState is CommentsScreenState.Comments) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                VkTopAppBar(
                    "$COMMENTS_FOR_POST_TITLE_SCAFFOLD_STR id: ${currentState.post.id}",
                    Icons.AutoMirrored.Filled.ArrowBack,
                    onIconClickListener = { onBackPressed() }
                )
            },
        ) { paddingValues ->
            LazyColumnCommentsForFeedPost(
                paddingValues,
                currentState.comments,
                currentState.post.contentText
            )
        }

    }
}

@Composable
fun LazyColumnCommentsForFeedPost(
    paddingValues: PaddingValues,
    postCommentsList: List<PostComment>,
    contentText: String,
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

        item {
            Text(contentText)
        }

        items(
            items = postCommentsList,
            key = { it.id }
        ) { comment ->

            CommentItem(comment)

        }
    }
}

@Composable
fun CommentItem(postComment: PostComment) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            painter = painterResource(postComment.authorAvatarId),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier,
            contentDescription = null
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
// todo нужно копнуть в paddingValues Scaffold bottom bar'a