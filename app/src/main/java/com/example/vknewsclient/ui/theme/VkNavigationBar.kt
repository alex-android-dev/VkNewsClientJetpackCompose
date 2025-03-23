package com.example.vknewsclient.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.example.vknewsclient.MainViewModel
import androidx.compose.runtime.getValue


@Composable
fun VkNavigationBar(viewModel: MainViewModel) {
    val items = listOf(NavigationItem.Home, NavigationItem.Message, NavigationItem.Settings)
    val selectedNavItem by viewModel.selectedNavItem.observeAsState(NavigationItem.Home)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedNavItem == item,
                onClick = { viewModel.selectedNavItem(item) },
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