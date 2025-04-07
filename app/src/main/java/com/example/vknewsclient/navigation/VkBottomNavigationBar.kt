package com.example.vknewsclient.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vknewsclient.presentation.main.NavigationItem


@Composable
fun VkBottomNavigationBar(navigationState: NavigationState) {
    val items =
        listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Settings)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
        // Хранит текущий открытый экран

        items.forEach { item ->

            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                it.route == item.screen.route
            } ?: false
            // Тут находится 3 наших иерархии
            // Вернет true если item совпадает с тем, что возвращает иерархия


            NavigationBarItem(
                selected = selected, // Совпадает ли открытый экран с элементом по которому был произведён клик
                onClick = {
                    if (!selected) navigationState.navigateTo(item.screen.route)
                    // Если элемент не отмеченный и мы кликаем по нему, то будет срабатывать. Иначе не будет срабатывать
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