package com.example.vknewsclient.ui.theme

import com.vk.id.VKIDAuthFail

sealed class AuthState {

    object Authorized : AuthState()
    object Initial : AuthState()

    object NonAuthorized : AuthState()
}