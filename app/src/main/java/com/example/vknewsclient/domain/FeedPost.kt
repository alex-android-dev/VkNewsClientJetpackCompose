package com.example.vknewsclient.domain

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.navigation.NavType
import com.example.vknewsclient.R
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class FeedPost(
    val id: Int = -1,
    val communityName: String = getRandomCommunityName(),
    val publicationDate: String = getRandomPublicationData(),
    val avatarResId: Int = R.drawable.community_avatar,
    var contentText: String = getRandomContentText(),
    val contentImageResId: Int = R.drawable.post_image_example,
    val statistics: List<StatisticItem> = getRandomStatistics(),
) : Parcelable {
    companion object {
        private fun getRandomCommunityName() =
            LoremIpsum(Random.nextInt(5, 10)).values.joinToString()

        @SuppressLint("DefaultLocale")
        private fun getRandomPublicationData() =
            String.format("%02d:%02d", Random.nextInt(24), Random.nextInt(59))

        private fun getRandomContentText() =
            LoremIpsum(Random.nextInt(10, 20)).values.joinToString()

        private fun getRandomStatistics() =
            listOf(
                StatisticItem(type = StatisticType.VIEWS, Random.nextInt(500)),
                StatisticItem(type = StatisticType.COMMENTS, Random.nextInt(50)),
                StatisticItem(type = StatisticType.SHARES, Random.nextInt(100)),
                StatisticItem(type = StatisticType.LIKES, Random.nextInt(300)),
            )

        // Переопределяем класс NavigationType
        val NavigationType: NavType<FeedPost> = object : NavType<FeedPost>(false) {
            // При помощи анонимного класса мы наследуемся от NavType и переопределяем необходимые методы

            // Получаем объекты из bundle.
            // Объект должен быть сериализуемым (которые можно превратить в набор байтов)
            // Используем Parcelable
            // КАК Получить
            override fun get(
                bundle: Bundle,
                key: String
            ): FeedPost? {
                return bundle.getParcelable(key)
            }

            // Преобразовать полученную строку в объект FeedPost
            // КАК распарсить строку
            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            // Кладём объект в bundle
            // КАК положить
            override fun put(
                bundle: Bundle,
                key: String,
                value: FeedPost
            ) {
                bundle.putParcelable(key, value)
            }
        }

    }
}