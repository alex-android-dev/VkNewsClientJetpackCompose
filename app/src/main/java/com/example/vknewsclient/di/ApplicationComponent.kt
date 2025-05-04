package com.example.vknewsclient.di

import android.app.Application
import com.example.vknewsclient.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    /**
     * При создании проекта Dagger Application Component сможет создать сабкомпонент
     * У Сабкомпонента мы вызовем метод create и закинем в него feedPost
     */
    fun getCommentsScreenComponentFactory(): CommentsScreenSubComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }

}

// TODO контекст. Понадобится ли?