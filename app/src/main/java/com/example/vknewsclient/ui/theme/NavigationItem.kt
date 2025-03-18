package com.example.vknewsclient.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vknewsclient.R

sealed class NavigationItem(
    val titleResId: Int,
    val icon: ImageVector
) {

    object Home: NavigationItem(
        titleResId = R.string.nav_item_main,
        icon = Icons.Filled.Home,
    )

    object Message: NavigationItem(
        titleResId = R.string.nav_item_message,
        icon = Icons.Filled.Email,
    )

    object Settings: NavigationItem(
        titleResId = R.string.nav_item_settings,
        icon = Icons.Filled.Settings,
    )

}