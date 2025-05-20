package com.example.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.presentation.getApplicationComponent
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.id.VKID

const val VK_TITLE_SCAFFOLD_STR = "VK Clone"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VKID.init(this)

        setContent {
            val daggerComponent = getApplicationComponent()
            Log.d("RECOMPOSITION_TAG", "MainActivity getApplicationComponent")

            val viewModel: MainViewModel = viewModel(
                factory = daggerComponent.getViewModelFactory()
            )

            /**
             * От данной функции зависит весь стейт экрана.
             * При её изменении - экран пересоздаётся
             * Сейчас при изменении стейта будет происходить рекомпозиция функции VkNewsClientTheme
             */
            val authState = viewModel.authState.collectAsState(AuthState.Initial)
            Log.d("MainActivity", "state ${authState.value}")


            MainActivityContent(
                authState = authState,
                viewModel = viewModel,
            )

        }
    }

    @Composable
    private fun MainActivityContent(
        authState: State<AuthState>,
        viewModel: MainViewModel,
    ) {
        VkNewsClientTheme() {
            when (authState.value) {
                is AuthState.Authorized -> VkNewsMainScreen(
                    backToAuthorize = {
//                        viewModel.refreshToken()
                        LoginScreen(viewModel)
                    }
                )

                is AuthState.NonAuthorized -> LoginScreen(viewModel)
                AuthState.Initial -> {}
            }
        }
    }

    companion object {
        private fun log(str: String) = Log.d("MainActivity", str)
    }
}