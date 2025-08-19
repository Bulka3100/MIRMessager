package com.example.mirmessager.feature.chat

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mirmessager.R
import com.example.mirmessager.data.model.Message
import com.example.mirmessager.ui.theme.DarkGrey
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(navController: NavController, channelId: String) {
    Scaffold(containerColor = Color.Black) {
        val viewModel: ChatViewModel = hiltViewModel()

        var alertDialogState by remember { mutableStateOf(false) }
        // что за класс такой (прочитать)

        var cameraImageUrl by remember { mutableStateOf<Uri?>(null) }

        val cameraImagelauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraImageUrl?.let {
                    viewModel.sendImageMessage(it,channelId)
                }

            }
        }

        // конкруетно тут разобрать стнтаксис работы
        fun createImageUri(): Uri {
            val timeStamp = SimpleDateFormat("yyyy.MMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = ContextCompat.getExternalFilesDirs(
                navController.context,
                Environment.DIRECTORY_PICTURES
            ).first()
            return FileProvider.getUriForFile(
                navController.context,
                "${navController.context.packageName}.provider",
                File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                    cameraImageUrl = Uri.fromFile(this)

                }
            )

        }


        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                cameraImagelauncher.launch(createImageUri())

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val message = viewModel.message.collectAsState()

            ChatMessages(message.value, { message ->
                viewModel.sendMessage(channelId, message)
            }, onSendImage = { alertDialogState = true }
            )

        }


        //РАЗОБРАТЬ ПОДТВЕРЖДЕНИЯ ПЕРМИШИНОВ И РАБОТЫ С ХРАНЕНИЕМ ФАЙЛОВ КАМЕРЫ
        if (alertDialogState) {
            ContentSelectedDialog(onCameraSelected = {
                alertDialogState = false
                if (navController.context.checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    cameraImagelauncher.launch(createImageUri())
                } else {
                    //requestpermission
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }, onGallerySelected = { alertDialogState = false })

        }
    }
}

@Composable
fun ContentSelectedDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
    AlertDialog(
        onDismissRequest = {/*TODO}*/ },
        confirmButton = {
            TextButton(onClick = onCameraSelected) {
                Text("Камера", color = Color.Black, modifier = Modifier.weight(1f))
            }
        },
        dismissButton = {
            TextButton(onClick = onCameraSelected) {
                Text("Галерея", color = Color.Black, modifier = Modifier.weight(1f))
            }
        },
        title = { Text("Выбери источник") },
        text = { Text(" Использовать изображение из галереии или из камеры?") }
    )
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onSendImage: () -> Unit,
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
                .background(color = Color.DarkGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                onSendImage()
                msg = ""
            }) {
                Image(
                    painter = painterResource(R.drawable.attach_svgrepo_com),
                    contentDescription = "attach"
                )
            }

            TextField(value = msg,
                onValueChange = { msg = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("напиши сообщение!") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboardController?.hide()

                    }
                ),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White


                )


            )
            IconButton(onClick = {
                onSendMessage(msg)
                msg = ""
            }) {
                Image(
                    painter = painterResource(R.drawable.send_alt_1_svgrepo_com),
                    contentDescription = "send"
                )
            }
        }
    }
}

//!!!!!!переносы вид сообщения
@Composable
fun ChatBubble(message: Message) {
    //зачем эта штука нужна вообще
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Blue
    } else {
        Color.DarkGray
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isCurrentUser) {
            Image(
                painter = painterResource(id = R.drawable.baseline_emoji_emotions_24),
                contentDescription = "userImage",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
            )
        }
        Box(
            modifier = Modifier
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            if (message.imageUrl != null) {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            } else {
                Text(
                    text = message.message?.trim() ?: "",
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }
       /* //разобрать модификаторы
        Box(
            modifier = Modifier
                //вот тут порядок !!!
                .widthIn(max = 280.dp)
                .padding(8.dp)
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                text = message.message?.trim() ?: "",
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)

            )
        }*/
        if (isCurrentUser) {
            Image(
                painter = painterResource(id = R.drawable.mir),
                contentDescription = "userImage",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showMessage() {

    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            ChatBubble(
                Message(
                    id = "1",
                    senderId = "1",
                    receiverId = "1",
                    message = "cucumber",
                    createdAt = System.currentTimeMillis(),
                    senderNameString = "1",
                    senderImage = null,
                    imageUrl = null,
                )
            )
        }
    }
}