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

@Composable
fun PostCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        shape = RoundedCornerShape(0.dp),
    ) {
        PostHeader()

        Spacer(modifier = Modifier.height(10.dp))

        PostBody()

        Spacer(modifier = Modifier.height(5.dp))

        Statistics()

    }
}

@Composable
private fun Statistics() {

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(modifier = Modifier.weight(1f)) {
            IconWithText("56", drawable.ic_views)
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconWithText("10", drawable.ic_share)
            IconWithText("3", drawable.ic_comment)
            IconWithText("33", drawable.ic_like)
        }

    }

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
private fun PostBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        Text(
            text = LoremIpsum(5).values.joinToString(),
        )

        Spacer(modifier = Modifier.height(5.dp))

        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(drawable.post_image_example),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )

    }
}

@Composable
private fun PostHeader() {

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
            painter = painterResource(drawable.community_avatar),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                text = "уволено",
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "14:00",
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
        PostCard()
    }
}


@Preview
@Composable
private fun PreviewPostCardDark() {
    VkNewsClientTheme(
        darkTheme = true
    ) {
        PostCard()
    }
}