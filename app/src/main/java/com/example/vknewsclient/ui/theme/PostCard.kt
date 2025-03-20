package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.R.drawable
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
) {
    Card(
        modifier = modifier,
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
            statisticItemList = feedPost.statistics
        )

    }
}

@Composable
private fun Statistics(statisticItemList: List<StatisticItem>) {

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statisticItemList.getItemByType(StatisticType.VIEWS)
            IconWithText(viewsItem.count.toString(), drawable.ic_views)
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val sharesItem = statisticItemList.getItemByType(StatisticType.SHARES)
            val commentsItem = statisticItemList.getItemByType(StatisticType.COMMENTS)
            val likesItem = statisticItemList.getItemByType(StatisticType.LIKES)

            IconWithText(sharesItem.count.toString(), drawable.ic_share)
            IconWithText(commentsItem.count.toString(), drawable.ic_comment)
            IconWithText(likesItem.count.toString(), drawable.ic_like)
        }

    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("ext fun getItemByType don't return correct type")
}

@Composable
private fun IconWithText(text: String, icon: Int) {

    Row {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Black500,
        )

        Spacer(modifier = Modifier.width(3.dp))

        Text(
            text = text,
            color = Black500,
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

        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(feedPost.contentImageResId),
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
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            painter = painterResource(feedPost.avatarResId),
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

@Preview
@Composable
private fun PreviewPostCardLight() {
    VkNewsClientTheme(
        darkTheme = false
    ) {
        PostCard(feedPost = FeedPost())
    }
}


@Preview
@Composable
private fun PreviewPostCardDark() {
    VkNewsClientTheme(
        darkTheme = true
    ) {
        PostCard(feedPost = FeedPost())
    }
}