package com.example.vknewsclient.presentation.main

sealed class AuthState {
    data class Authorized(val token: String) : AuthState()
    object Initial : AuthState()
    object NonAuthorized : AuthState()
}