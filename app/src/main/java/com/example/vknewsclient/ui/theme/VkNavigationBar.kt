package com.example.vknewsclient.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun VkNavigationBar(navHostController: NavHostController) {

    val items =
        listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Settings)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState() // Хранит текущий открытый экран
        val currentRoute =
            navBackStackEntry?.destination?.route // Получаем название экрана, который сейчас открыт

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route, // Совпадает ли открытый экран с элементом по которому был произведён клик
                onClick = {
                    navHostController.navigate(item.screen.route) // Передаем путь к экрану
                },
                icon = { Icon(item.icon, contentDescription = item.icon.name) },
                label = { Text(stringResource(item.titleResId)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }
    }

}