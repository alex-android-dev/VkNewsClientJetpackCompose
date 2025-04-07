package com.example.vknewsclient.presentation.samples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SideEffectTest(number: MyNumber) { // number приходит 10
    Column()
    {
        LazyColumn {
            repeat(5) {

                item {
                    Text(text = "Number ${number.value}")
                }

            }
        }


        number.value = 5


        Spacer(Modifier.height(20.dp))

        LazyColumn {
            repeat(5) {

                item {
                    Text(text = "Number ${number.value}")
                }

            }
        }
    }
}

data class MyNumber(var value: Int)