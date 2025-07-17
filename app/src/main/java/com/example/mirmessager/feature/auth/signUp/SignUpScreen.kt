package com.example.mirmessager.feature.auth.signUp


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mirmessager.MainApp
import com.example.mirmessager.R
import com.example.mirmessager.feature.auth.signIn.SignInState

@Composable
fun SignUpScreen(navController: NavController) {
    //нормально ли так объявлять viewmodel
    val viewModel: SignUpViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    when (state.value) {
        is SignUpState.Success -> {
            navController.navigate("home")
        }

        is SignUpState.Error -> {
            Toast.makeText(context, "ошибка регистрации", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        //так можно делать не прописывая лямбду напрямую типо innerPadding->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            //способ дя изображения
            Image(
                painter = painterResource(id = R.drawable.mir), contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                onValueChange = { email = it },
                value = email,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                onValueChange = { name = it },
                value = name,
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                onValueChange = { password = it },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth()

            )
            OutlinedTextField(
                onValueChange = { confirmPassword = it },
                value = confirmPassword,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Подтвердить пароль") },
                isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (state.value == SignUpState.Loading) {
                CircularProgressIndicator()
            } else{
                Button(
                    onClick = { viewModel.signUp(name, email, password) },
                    enabled = password.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && password == confirmPassword,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Зарегистрироваться")


                }
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Уже есть аккаунт? Войти")
                }
            }
        }
    }

            }
//                почеум тут только в одном месте проверку поставил и почему именно такую проверку. Как сделать проверку пароля на слложность например тут и выводить тосты на экран в чем проблема?


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun showUi() {
    MaterialTheme {
        SignUpScreen(navController = rememberNavController())
    }
}
