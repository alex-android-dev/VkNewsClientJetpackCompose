package com.example.vknewsclient.domain

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.vknewsclient.R

data class FeedPost(
    val communityName: String = "communityName",
    val publicationDate: String = "00:00",
    val avatarResId: Int = R.drawable.community_avatar,
    val contentText: String = LoremIpsum(10).values.joinToString(),
    val contentImageResId: Int = R.drawable.post_image_example,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, 966),
        StatisticItem(type = StatisticType.COMMENTS, 13),
        StatisticItem(type = StatisticType.SHARES, 56),
        StatisticItem(type = StatisticType.LIKES, 102),
    )
)