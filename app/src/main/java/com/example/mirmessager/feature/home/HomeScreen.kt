package com.example.mirmessager.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    // это то же самое что и val viewModel  = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    var addChannel by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(floatingActionButton = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue)
                .clickable { addChannel = true }
        ) {
            Text(
                text = "создать канал",
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
        }
    }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(channels.value) { channel ->
                    Column {
                        Text(text = channel.name, color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Blue)
                                .clickable { navController.navigate("") }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
    if (addChannel) {
        ModalBottomSheet(onDismissRequest = {}, sheetState = sheetState) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel = false
            }
        }
    }
}

@Composable
// вот тут реально не разобрался что куда передается и какими значениями важно глянуть TODO
fun AddChannelDialog(onAddChanel: (String) -> Unit) {
    var channelName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Добавить канал ")
        Spacer(modifier = Modifier.size(8.dp))
        TextField(
            value = channelName,
            onValueChange = { channelName = it },
            label = { Text("Название канала") },
            singleLine = true
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = { onAddChanel(channelName) }, modifier = Modifier.fillMaxWidth()) {
            Text("Создать")
        }


    }

}


