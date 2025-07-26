package com.example.mirmessager.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true, )
@Composable
fun Test() {
    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .border(2.dp,Color.Red)
                .padding(8.dp)
                .border(2.dp,Color.Red)

                .background(Color.Blue)

                .clickable {  }
        ) {
            Text("Создать канал")
        }
        Spacer(Modifier.height(200.dp))
        Box(
            contentAlignment = Alignment.Center,

            modifier = Modifier
                .height(100.dp)
                .padding(8.dp)
                .border(2.dp,Color.Red)

                .fillMaxWidth()
                .border(2.dp,Color.Red)
                .background(Color.Blue)
        ) {
            Text("Создать канал")
        }
        Spacer(Modifier.height(200.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .padding(8.dp)
                .clickable {  }
        ) {
            Text("Создать канал")
        }
    }
}