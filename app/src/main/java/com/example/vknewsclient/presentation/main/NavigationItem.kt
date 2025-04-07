package com.example.vknewsclient.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vknewsclient.R
import com.example.vknewsclient.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector,
) {

    object Home : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.nav_item_main,
        icon = Icons.Filled.Home,
    )

    object Favourite : NavigationItem(
        screen = Screen.Favorite,
        titleResId = R.string.nav_item_favourite,
        icon = Icons.Filled.Favorite,
    )

    object Settings : NavigationItem(
        screen = Screen.Profile,
        titleResId = R.string.nav_item_profile,
        icon = Icons.Filled.Settings,
    )

}