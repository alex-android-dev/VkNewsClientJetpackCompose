package com.example.vknewsclient.domain

import com.example.vknewsclient.R

data class FeedPost(
    val id: Int = -1,
    val communityName: String = "dev.ru",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.community_avatar,
    val contentText: String = "this is content",
    val contentImageResId: Int = R.drawable.post_image_example,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(type = StatisticType.VIEWS, 12),
        StatisticItem(type = StatisticType.COMMENTS, 31),
        StatisticItem(type = StatisticType.SHARES, 12),
        StatisticItem(type = StatisticType.LIKES, 55),
    )
)