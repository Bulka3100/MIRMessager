package com.example.mirmessager.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import com.example.mirmessager.ui.theme.DarkGrey
import kotlinx.coroutines.channels.Channel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    // это то же самое что и val viewModel  = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    var addChannel by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        floatingActionButton = {
            Box(

                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Blue)
                    .clickable { addChannel = true }


            ) {
                Text(
                    text = "создать канал",
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }, containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    Text(
                        text = "Чаты",
                        color = Color.Gray,
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Black),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                item {
                    TextField(
                        onValueChange = {},
                        value = "",
                        placeholder = { Text("поиск") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)

                            .clip(
                                RoundedCornerShape(21.dp)
                            ),
                        colors = TextFieldDefaults.colors().copy(
                            // Цвет фона TextField
                            focusedContainerColor = DarkGrey,    // Когда поле в фокусе
                            unfocusedContainerColor = DarkGrey,  // Когда поле не в фокусе

                            // Цвет текста
                            focusedTextColor = Color.Gray,       // Текст при фокусе
                            unfocusedTextColor = Color.Gray,     // Текст без фокуса

                            // Цвет плейсхолдера (подсказки)
                            focusedPlaceholderColor = Color.Gray,    // Плейсхолдер при фокусе
                            unfocusedPlaceholderColor = Color.Gray,  // Плейсхолдер без фокуса

                            // Цвет нижней линии (индикатора)
                            focusedIndicatorColor = Color.Blue,   // Линия при фокусе
                            unfocusedIndicatorColor = Color.Blue
                        ), trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search, contentDescription = ""
                            )
                        }
                    )
                }
                items(channels.value) { channel ->
                    ChannelItem(channel.name, { navController.navigate("chat/${channel.id}") })


                }
            }
        }
    }
    if (addChannel) {
        ModalBottomSheet(onDismissRequest = { addChannel = false }, sheetState = sheetState) {
            AddChannelDialog { channelName ->
                viewModel.addChannel(channelName)
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

@Composable
fun ChannelItem(channelName: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(DarkGrey)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp),

        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = channelName[0].toString().uppercase(),
                style = TextStyle(fontSize = 35.sp, fontWeight = FontWeight.Black)
            )
        }
        Text(
            text = channelName,
            style = TextStyle(fontSize = 32.sp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showItem() {

    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            ChannelItem("test", {})
        }
    }
}
