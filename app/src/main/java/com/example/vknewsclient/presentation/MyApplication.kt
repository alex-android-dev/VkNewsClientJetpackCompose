package com.example.vknewsclient.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.vknewsclient.di.ApplicationComponent
import com.example.vknewsclient.di.DaggerApplicationComponent

class MyApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}

/**
 * Сделали Composable функцию для получения компонента
 * Делаем для того, чтобы не получать несколько раз данные из компонента
 */

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as MyApplication).component
}

