package com.example.vknewsclient.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState = _authState

    init {
        var token : String? = null

        token = VKID.Companion.instance.accessToken?.token
        Log.d("MainViewModel", "token: ${VKID.Companion.instance.accessToken?.token}")

        viewModelScope.launch {
            getUserData()
            // TODO доделать refreshToken
        }

        if (token != null) {
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NonAuthorized
        }
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

    private suspend fun getUserData() {
        VKID.instance.getUserData(
            callback = object : VKIDGetUserCallback {
                override fun onSuccess(user: VKIDUser) {
                    Log.d("MainViewModel", "$user")
                }

                override fun onFail(fail: VKIDGetUserFail) {
                    when (fail) {
                        is VKIDGetUserFail.FailedApiCall -> fail.description // Использование текста ошибки.
                        is VKIDGetUserFail.IdTokenTokenExpired -> fail.description // Использование текста ошибки.
                        is VKIDGetUserFail.NotAuthenticated -> fail.description // Использование текста ошибки.
                    }
                }
            }
        )
    }

    private suspend fun refreshToken() {
        VKID.instance.refreshToken(
            callback = object : VKIDRefreshTokenCallback {
                override fun onSuccess(token: AccessToken) {
                    Log.d("MainViewModel", "refresh token: $token")
                }

                override fun onFail(fail: VKIDRefreshTokenFail) {
                    when (fail) {
                        is VKIDRefreshTokenFail.FailedApiCall -> fail.description // Использование текста ошибки.
                        is VKIDRefreshTokenFail.FailedOAuthState -> fail.description // Использование текста ошибки.
                        is VKIDRefreshTokenFail.RefreshTokenExpired -> fail // Ошибка истечения срока жизни RT. Это уведомление о том, что пользователю нужно перелогиниться.
                        is VKIDRefreshTokenFail.NotAuthenticated -> fail // Ошибка отсутствия авторизации у пользователя. Это уведомление о том, что пользователю нужно авторизоваться.
                    }
                }
            }
        )
    }
}

