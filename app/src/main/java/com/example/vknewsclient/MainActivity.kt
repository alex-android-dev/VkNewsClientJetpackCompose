package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.vknewsclient.ui.theme.PostCard
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme() {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    topBar = {
                        VkTopAppBar()
                    },
                    bottomBar = {
                        VkNavigationBar()
                    }
                ) { paddingValues ->

                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                    ) {
                        PostCard()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VkTopAppBar() {
    TopAppBar(
        title = { Text(text = "VK clone") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun VkNavigationBar() {
//    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Message", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Email, Icons.Filled.Settings)

//    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Outlined.Email, Icons.Outlined.Settings)

    NavigationBar(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) {
        items.forEachIndexed { index, item ->4
            NavigationBarItem(
                icon = {
                    Icon(
                        selectedIcons[index],
                        contentDescription = item,
                    )
                },
                label = { Text(item) },
                selected = true,
                onClick = {}
            )
        }
    }
}