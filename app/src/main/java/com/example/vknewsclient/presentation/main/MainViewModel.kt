package com.example.vknewsclient.presentation.main

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail

class MainViewModel() : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState = _authState

    init {
        val token = VKID.Companion.instance.accessToken?.token

        if (token != null) {
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NonAuthorized
        }
    }

    fun saveToken(context: Context, token: AccessToken) {
        saveToken(context, token.token)
    }

    fun performAuthResult(state: AuthState) {
        _authState.value = state
    }

    fun setFail(fail: VKIDAuthFail) {
        when (fail) {
            is VKIDAuthFail.Canceled -> Log.d("LoginScreen", "Failed Canceled")
            is VKIDAuthFail.FailedApiCall -> Log.d("LoginScreen", "Failed ApiCall")
            is VKIDAuthFail.FailedOAuthState -> Log.d(
                "LoginScreen",
                "Failed OAuthState"
            )

            is VKIDAuthFail.FailedRedirectActivity -> Log.d(
                "LoginScreen",
                "Failed RedirectActivity"
            )

            is VKIDAuthFail.NoBrowserAvailable -> Log.d(
                "LoginScreen",
                "NoBrowserAvailable"
            )

            else -> Log.d("LoginScreen", "Another Error")
        }

    }

    private fun saveToken(context: Context, token: String) {
        val sharedPreferences =
            context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit() { putString("user_token", token) }
    }
}

