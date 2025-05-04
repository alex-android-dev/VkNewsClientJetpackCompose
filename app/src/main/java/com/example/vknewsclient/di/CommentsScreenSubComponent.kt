package com.example.vknewsclient.di

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

/**
 * Сабкомпонент в данном случае необходим, чтобы мы могли передавать фидпост в VM комментариев
 * На момент создания компонента зависимости(фидпост) будет присутствовать
 *
 * Поскольку CommentsViewModel нам понадобится только в момент создания данного компонента
 * То добавим модуль этой ViewModel сюда
 */

@Subcomponent(modules = [CommentsViewModelModule::class])
interface CommentsScreenSubComponent {
    /**
     * Поскольку у нас проект на Compose и мы не можем ViewModel заинжектить в поля.
     * Следовательно метод для возвращения VMFactory делаем тут
     */
    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance feedPost: FeedPost,
        ): CommentsScreenSubComponent
    }

}