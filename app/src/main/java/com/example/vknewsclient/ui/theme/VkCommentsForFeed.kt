package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.domain.PostComment

const val COMMENTS_FOR_POST_TITLE_SCAFFOLD_STR = "Comments for Post"

@Composable
fun VkCommentsForFeedPosts(
    postCommentsList: List<PostComment>,
    postId: Int,
) {

    Scaffold(
        topBar = {
            VkTopAppBar(
                "$COMMENTS_FOR_POST_TITLE_SCAFFOLD_STR id: $postId",
                Icons.AutoMirrored.Filled.ArrowBack,
            )
        },
    ) { paddingValues ->
        LazyColumnCommentsForFeedPost(paddingValues, postCommentsList)
    }

}

@Composable
fun LazyColumnCommentsForFeedPost(
    paddingValues: PaddingValues,
    postCommentsList: List<PostComment>
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
        ) { postComment ->

            PostComment(postComment)

        }
    }


}

@Composable
fun PostComment(postComment: PostComment) {

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

        Spacer(modifier = Modifier.width(5.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(postComment.authorName)
            Text(postComment.commentText)
            Text(postComment.publicationDate)
        }
    }
}

@Preview
@Composable
fun PreviewPostComment() {

    val postCommentsList = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(
                PostComment(
                    id = it,
                    authorName = "author number $it",
                )
            )
        }
    }.toList()


    VkCommentsForFeedPosts(postCommentsList, 33)

}