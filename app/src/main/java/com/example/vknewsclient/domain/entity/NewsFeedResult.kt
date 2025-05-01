package com.example.vknewsclient.domain.entity

/** Класс используется для примера
 * Если у нас не прогружаются данные и прилетает ошибка при загрузке данных **/

// TODO доделать функциональность. Если прилетает ошибка, то отправить на экран авторизации
sealed class NewsFeedResult {
    object Error : NewsFeedResult()
    data class Success(val posts: List<FeedPost>) : NewsFeedResult()
}