package com.example.vknewsclient.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VkTopAppBar(
    title: String,
    imageVector: ImageVector,
    onIconClickListener: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onIconClickListener()
                        },
                    imageVector = imageVector,
                    contentDescription = null
                )
            }
        }
    )
}