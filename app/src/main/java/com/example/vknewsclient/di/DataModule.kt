package com.example.vknewsclient.di

import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.data.repository.RepositoryImpl
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindDataRepository(impl: RepositoryImpl): Repository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService() = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideFeedPost() = FeedPost(
            id = 0,
            communityId = 0,
            communityName = "",
            publicationDate = "",
            communityImageUrl = "",
            contentText = "",
            contentImageUrl = "",
            statistics = listOf(),
            isLiked = false
        )

        @ApplicationScope
        @Provides
        fun provideVkStorage(): Nothing {
            TODO(
                "Метод отвечает за выдачу токена. Сейчас токен выдается внутри data слоя." +
                        " Доделать реализацию"
            )
        }

    }

}