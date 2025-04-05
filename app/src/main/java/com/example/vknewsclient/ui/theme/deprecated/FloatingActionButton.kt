package com.example.vknewsclient.ui.theme.deprecated

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun VkFloatingActionButton(
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    fabIsVisible: MutableState<Boolean>,
) {
    val icon = Icons.Filled.Favorite

    FloatingActionButton(
        modifier = Modifier
            .padding(bottom = 12.dp),
        onClick = {
            showSnackBar(scope, snackBarHostState, fabIsVisible)
        },
        containerColor = MaterialTheme.colorScheme.onBackground
    ) {
        Icon(
            icon,
            contentDescription = icon.name,
            tint = Color.Red
        )
    }
}

private fun showSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    fabIsVisible: MutableState<Boolean>,
) {
    scope.launch {
        val action = snackBarHostState.showSnackbar(
            message = "This is SnackBar",
            actionLabel = "Hide FAB",
            duration = SnackbarDuration.Long
        )

        if (action == SnackbarResult.ActionPerformed) {
            fabIsVisible.value = false
        }
    }
}
