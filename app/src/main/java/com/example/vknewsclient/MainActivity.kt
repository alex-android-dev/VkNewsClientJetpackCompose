package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.ui.theme.PostCard
import com.example.vknewsclient.ui.theme.VkNavigationBar
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import com.example.vknewsclient.ui.theme.VkTopAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme() {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val feedPost = remember {
        mutableStateOf(FeedPost())
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            VkTopAppBar()
        },
        bottomBar = {
            VkNavigationBar()
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            PostCard(
                modifier = Modifier.padding(8.dp),
                feedPost.value,
                onStatisticsItemClickListener = { newStatisticItem ->
                    val oldStatistics = feedPost.value.statistics

                    val newStatistics = oldStatistics.toMutableList().apply {
                        replaceAll { oldItem ->
                            if (oldItem.type == newStatisticItem.type)
                                oldItem.copy(count = oldItem.count + 1)
                            else oldItem
                        }
                    }


                    feedPost.value = feedPost.value.copy(statistics = newStatistics)
                }
            )
        }
    }
}