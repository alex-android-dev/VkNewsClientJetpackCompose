package com.example.vknewsclient.di

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component


@Component(modules = [DataModule::class])
interface ApplicationComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context)
    }

}

// TODO контекст. Понадобится ли?