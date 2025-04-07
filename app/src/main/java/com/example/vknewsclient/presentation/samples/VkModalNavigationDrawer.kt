package com.example.vknewsclient.presentation.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VkModalNavigationDrawer() {

    val items = listOf(
        Icons.Default.AccountCircle,
        Icons.Default.Settings
    )

    ModalDrawerSheet {
        Column {
            Spacer(Modifier.height(10.dp))
            items.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item, contentDescription = null) },
                    label = { Text(item.name.substringAfterLast(".")) },
                    selected = false,
                    onClick = {},
                )
            }
        }
    }
}