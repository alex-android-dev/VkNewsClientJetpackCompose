package com.example.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import com.example.vknewsclient.ui.theme.MainScreen
import com.example.vknewsclient.ui.theme.MyNumber
import com.example.vknewsclient.ui.theme.SideEffectTest
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

const val VK_TITLE_SCAFFOLD_STR = "VK Clone"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val vkScopeObjects = listOf(VKScope.WALL, VKScope.PHOTOS)


            val launcher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract(), // Передаем сюда контракт от ВК
                onResult = { result ->
                    when (result) {
                        is VKAuthenticationResult.Success -> {
                            log("Success auth")
                        }

                        is VKAuthenticationResult.Failed -> {
                            log("Failed auth")
                        }
                    }
                }
            )
            SideEffect {
                launcher.launch(vkScopeObjects)
            }

            VkNewsClientTheme() {
                MainScreen()
            }
        }
    }

    companion object {
        private fun log(str: String) = Log.d("MainActivity", str)
    }
}