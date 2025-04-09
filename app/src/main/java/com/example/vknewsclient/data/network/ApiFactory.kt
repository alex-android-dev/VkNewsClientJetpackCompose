package com.example.vknewsclient.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val BASE_URL = "https://api.vk.ru/method/"

object ApiFactory {

    // Позволит нам смотреть запросы и ответы
    // Если произойдет какая-то ошибка, то мы её сможем отследить
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor( // Добавляем интерсептор
            HttpLoggingInterceptor().apply {
                // Выводим уровень логирования. Что он будет выводить в лог
                level = HttpLoggingInterceptor.Level.BODY
            })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()

}

// Теперь мы можем делать запросы и получать ответы