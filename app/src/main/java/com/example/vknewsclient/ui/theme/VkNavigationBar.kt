package com.example.vknewsclient.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VkNavigationBar() {
    val items = listOf(NavigationItem.Home, NavigationItem.Message, NavigationItem.Settings)

    val selectedItemPosition = remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemPosition.intValue == index,
                onClick = { selectedItemPosition.intValue = index },
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

@Preview
@Composable
private fun PreviewPostCardLight() {
    VkNewsClientTheme(
        darkTheme = false
    ) {
        VkNavigationBar()
    }
}


@Preview
@Composable
private fun PreviewPostCardDark() {
    VkNewsClientTheme(
        darkTheme = true
    ) {
        VkNavigationBar()
    }
}