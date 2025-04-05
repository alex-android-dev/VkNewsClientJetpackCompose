package com.example.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

                when (authState.value) {
                    is AuthState.Authorized -> VkNewsMainScreen()
                    is AuthState.NonAuthorized -> LoginScreen(this, viewModel)
                    else -> {}
                }

            }
        }
    }

    companion object {
        private fun log(str: String) = Log.d("MainActivity", str)
    }
}