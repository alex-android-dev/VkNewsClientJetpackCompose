package com.example.vknewsclient.presentation.main

sealed class AuthState {

    object Authorized : AuthState()
    object Initial : AuthState()

    object NonAuthorized : AuthState()
}