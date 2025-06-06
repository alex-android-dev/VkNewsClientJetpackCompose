package com.example.vknewsclient.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.vknewsclient.R.drawable
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import com.example.vknewsclient.ui.theme.Black500
import com.example.vknewsclient.ui.theme.DarkRed

@Composable
fun PostCard(
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RoundedCornerShape(0.dp),
    ) {
        PostHeader(feedPost)

        Spacer(modifier = Modifier.height(10.dp))

        PostBody(feedPost)

        Spacer(modifier = Modifier.height(5.dp))

        Statistics(
            statisticItemList = feedPost.statistics,
            onLikeClickListener = onLikeClickListener,
            onCommentClickListener = onCommentClickListener,
            isFavorite = feedPost.isLiked,
        )

    }
}

@Composable
private fun Statistics(
    statisticItemList: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    isFavorite: Boolean,
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statisticItemList.getItemByType(StatisticType.VIEWS)

            IconWithText(
                text = formatStatisticCount(viewsItem.count),
                icon = drawable.ic_views,
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            val sharesItem = statisticItemList.getItemByType(StatisticType.SHARES)
            IconWithText(
                text = formatStatisticCount(sharesItem.count),
                icon = drawable.ic_share,
            )

            val commentsItem = statisticItemList.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                text = formatStatisticCount(commentsItem.count),
                icon = drawable.ic_comment,
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                })

            val likesItem = statisticItemList.getItemByType(StatisticType.LIKES)
            IconWithText(
                text = formatStatisticCount(likesItem.count),
                icon = if (isFavorite) drawable.ic_like_set
                else drawable.ic_like,
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                },
                tint = if (isFavorite) DarkRed else Black500
            )


        }

    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", count / 1_000)
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1_000f))
    } else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("ext fun getItemByType don't return correct type")
}

@Composable
private fun IconWithText(
    text: String,
    icon: Int,
    onItemClickListener: (() -> Unit)? = null,
    tint: Color = Black500,
) {
    val modifier = if (onItemClickListener == null) Modifier
    else Modifier.clickable {
        onItemClickListener()
    }

    Row(
        modifier = modifier
    ) {

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
        )

        Spacer(modifier = Modifier.width(3.dp))

        Text(
            text = text,
            color = tint,
        )

    }

}

@Composable
private fun PostBody(feedPost: FeedPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = feedPost.contentText,
        )

        Spacer(modifier = Modifier.height(5.dp))

        AsyncImage(
            model = feedPost.contentImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )

    }
}

@Composable
private fun PostHeader(feedPost: FeedPost) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = feedPost.communityImageUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text = feedPost.communityName
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = feedPost.publicationDate,
                color = Black500,
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
        )
    }


}