package com.example.mirmessager.feature.chat

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mirmessager.data.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ChatScreen(navController: NavController, channelId: String) {
    Scaffold {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            val viewModel: ChatViewModel = hiltViewModel()
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val message = viewModel.message.collectAsState()

            ChatMessages(message.value, { message ->
                viewModel.sendMessage(channelId, message)
            }
            )
        }
    }
}


@Composable
fun ChatMessages(
    messages: List<Message>,
    //что это за л  ямбда
    onSendMessage: (String) -> Unit,
) {
    val hideKeyboardController = LocalSoftwareKeyboardController.current
    var msg by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .background(color = Color.LightGray),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(value = msg,
                onValueChange = { msg = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("напиши сообщение!") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboardController?.hide()

                    }
                )
            )
            IconButton(onClick = {
                onSendMessage(msg)
                msg = ""
            }) { Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send") }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    //зачем эта штука нужна вообще
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Blue
    } else {
        Color.Green
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = message.message, color = Color.White, modifier = Modifier.padding(8.dp))
        }
    }
}
