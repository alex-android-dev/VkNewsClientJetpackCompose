package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TextCounter(name: String, paddingValues: PaddingValues) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Text(
        modifier = Modifier
            .clickable { count++ }
            .padding(paddingValues),
        text = "$name Count: $count",
        color = Color.Black,

        )
}