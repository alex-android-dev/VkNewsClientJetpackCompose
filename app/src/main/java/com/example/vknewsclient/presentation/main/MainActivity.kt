package com.example.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.id.VKID

const val VK_TITLE_SCAFFOLD_STR = "VK Clone"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.init(this)

        setContent {
            VkNewsClientTheme() {
                val viewModel: MainViewModel = viewModel()
                var authState = viewModel.authState.collectAsState(AuthState.Initial)
                Log.d("MainActivity", "state ${authState.value}")

                when (authState.value) {
                    is AuthState.Authorized -> VkNewsMainScreen(
                        backToAuthorize = {
                            viewModel.refreshToken()
                        }
                    )

                    is AuthState.NonAuthorized -> LoginScreen(viewModel)
                    AuthState.Initial -> {}
                }
            }
        }
    }

    companion object {
        private fun log(str: String) = Log.d("MainActivity", str)
    }
}