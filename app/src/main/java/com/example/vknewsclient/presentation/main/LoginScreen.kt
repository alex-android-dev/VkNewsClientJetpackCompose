package com.example.vknewsclient.presentation.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.R
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Composable
fun LoginScreen(
    viewModel: MainViewModel
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.ic_vk),
                contentDescription = "vk logo"
            )
            Spacer(modifier = Modifier.height(100.dp))

            OneTap(
                onAuth = { oAuth, token ->
                    Log.d("LoginScreen", "token: ${token.token}")
                    viewModel.performAuthResult(AuthState.Authorized(token.token))
                },
                onFail = { oAuth, fail ->
                    viewModel.performAuthResult(AuthState.NonAuthorized)
                    viewModel.setFail(fail)
                },
                authParams = VKIDAuthUiParams {
                    scopes = setOf("wall", "email", "phone_number", "friends")
                },
                scenario = OneTapTitleScenario.SignUp,
                signInAnotherAccountButtonEnabled = true,
            )
        }
    }
}