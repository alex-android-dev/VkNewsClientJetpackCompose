package com.example.vknewsclient.domain

import android.os.Bundle
import android.os.Parcelable
import android.provider.SimPhonebookContract
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class FeedPost(
    val id: String,
    val communityName: String,
    val publicationDate: String,
    val communityImageUrl: String,
    var contentText: String,
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isFavorite: Boolean,
) : Parcelable {
    companion object {
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