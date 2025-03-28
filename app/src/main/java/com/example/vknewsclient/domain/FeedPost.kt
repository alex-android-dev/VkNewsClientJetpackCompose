package com.example.vknewsclient.domain

import android.annotation.SuppressLint
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.vknewsclient.R
import kotlin.random.Random

data class FeedPost(
    val id: Int = -1,
    val communityName: String = getRandomCommunityName(),
    val publicationDate: String = getRandomPublicationData(),
    val avatarResId: Int = R.drawable.community_avatar,
    var contentText: String = getRandomContentText(),
    val contentImageResId: Int = R.drawable.post_image_example,
    val statistics: List<StatisticItem> = getRandomStatistics(),
) {
    private companion object {
        fun getRandomCommunityName() =
            LoremIpsum(Random.nextInt(5, 10)).values.joinToString()

        @SuppressLint("DefaultLocale")
        fun getRandomPublicationData() =
            String.format("%02d:%02d", Random.nextInt(24), Random.nextInt(59))

        fun getRandomContentText() =
            LoremIpsum(Random.nextInt(10, 20)).values.joinToString()

        fun getRandomStatistics() =
            listOf(
                StatisticItem(type = StatisticType.VIEWS, Random.nextInt(500)),
                StatisticItem(type = StatisticType.COMMENTS, Random.nextInt(50)),
                StatisticItem(type = StatisticType.SHARES, Random.nextInt(100)),
                StatisticItem(type = StatisticType.LIKES, Random.nextInt(300)),
            )

    }
}