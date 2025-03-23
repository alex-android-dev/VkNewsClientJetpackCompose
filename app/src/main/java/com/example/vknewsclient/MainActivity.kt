package com.example.vknewsclient

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.vknewsclient.ui.theme.VkNavigationBar
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.example.vknewsclient.ui.theme.HomeScreen
import com.example.vknewsclient.ui.theme.NavigationItem
import com.example.vknewsclient.ui.theme.VkTopAppBar

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme() {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val selectedNavItem by viewModel.selectedNavItem.observeAsState(NavigationItem.Home)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            VkTopAppBar()
        },
        bottomBar = {
            VkNavigationBar(viewModel)
        },
    ) { paddingValues ->

        when (selectedNavItem) {
            NavigationItem.Home -> HomeScreen(viewModel, paddingValues)
            NavigationItem.Message -> Text(
                "View Message",
                color = Color.Black,
                modifier = Modifier.padding(paddingValues)
            )

            NavigationItem.Settings -> Text(
                "View Settings",
                color = Color.Black,
                modifier = Modifier.padding(paddingValues)
            )
        }


    }
}