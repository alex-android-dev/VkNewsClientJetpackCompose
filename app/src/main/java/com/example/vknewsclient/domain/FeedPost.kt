package com.example.vknewsclient.domain

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.vknewsclient.R
import kotlin.random.Random

data class FeedPost(
    val id: Int = -1,
    val communityName: String = LoremIpsum(5).values.joinToString(),
    val publicationDate: String = String.format("%02d:%02d", Random.nextInt(24), Random.nextInt(59)),
    val avatarResId: Int = R.drawable.community_avatar,
    val contentText: String = LoremIpsum(10).values.joinToString(),
    val contentImageResId: Int = R.drawable.post_image_example,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, Random.nextInt(500)),
        StatisticItem(type = StatisticType.COMMENTS, Random.nextInt(50)),
        StatisticItem(type = StatisticType.SHARES, Random.nextInt(100)),
        StatisticItem(type = StatisticType.LIKES, Random.nextInt(300)),
    )
) 