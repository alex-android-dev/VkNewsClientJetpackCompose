package com.example.vknewsclient.presentation.samples

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun VkSnackBarHost(snackBarHostState: SnackbarHostState) {

    SnackbarHost(
        hostState = snackBarHostState
    )


}