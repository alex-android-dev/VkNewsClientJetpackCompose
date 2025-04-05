package com.example.vknewsclient.ui.theme

import android.content.Context
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
import com.example.vknewsclient.MainViewModel
import com.example.vknewsclient.R
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Composable
fun LoginScreen(
    context: Context,
    viewModel: MainViewModel,
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
                    // TODO UI слой не должен знать об domain слое. Нужно подумать как переделать
                    viewModel.performAuthResult(AuthState.Authorized)
                    viewModel.saveToken(context, token)
                },
                onFail = { oAuth, fail ->
                    // TODO UI слой не должен знать об domain слое. Нужно подумать как переделать
                    viewModel.performAuthResult(AuthState.NonAuthorized)
                    viewModel.setFail(fail)
                },
                scenario = OneTapTitleScenario.SignUp,
                signInAnotherAccountButtonEnabled = true,
            )


//            Button(
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = DarkBlue,
//                    contentColor = Color.White
//                ),
//                onClick = { onLoginClick() }
//            ) {
//                Text(
//                    text = stringResource(R.string.button_login)
//                )
//            }
        }
    }
}