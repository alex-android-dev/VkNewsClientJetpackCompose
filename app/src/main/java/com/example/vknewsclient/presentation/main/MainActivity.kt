package com.example.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.presentation.MyApplication
import com.example.vknewsclient.presentation.ViewModelFactory
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.id.VKID
import javax.inject.Inject

const val VK_TITLE_SCAFFOLD_STR = "VK Clone"

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val daggerComponent by lazy {
        (application as MyApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daggerComponent.inject(this)
        VKID.init(this)

        setContent {
            VkNewsClientTheme() {
                daggerComponent.inject(this)

                val viewModel: MainViewModel = viewModel(
                    factory = viewModelFactory
                )


                var authState = viewModel.authState.collectAsState(AuthState.Initial)
                Log.d("MainActivity", "state ${authState.value}")

                when (authState.value) {
                    is AuthState.Authorized -> VkNewsMainScreen(
                        viewModelFactory,
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