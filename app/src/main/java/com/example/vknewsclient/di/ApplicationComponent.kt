package com.example.vknewsclient.di

import android.app.Application
import android.content.Context
import com.example.vknewsclient.presentation.main.MainActivity
import dagger.Binds
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }

}

// TODO контекст. Понадобится ли?