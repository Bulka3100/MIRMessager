package com.example.mirmessager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mirmessager.feature.auth.signUp.SignUpScreen
import com.example.mirmessager.feature.auth.signIn.SignInScreen
import com.example.mirmessager.feature.chat.ChatScreen
import com.example.mirmessager.feature.home.HomeScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun MainApp(){
Surface(modifier = Modifier.fillMaxSize()) {
    val navController = rememberNavController()
//        есть ли разница в получении пользователя
    val currentUser = Firebase.auth.currentUser
    val currentUser1 = FirebaseAuth.getInstance().currentUser

    val routeStart = if (currentUser1 == null){"login" }
    else{"home"}



    NavHost(navController = navController, startDestination = routeStart){
        composable("login") {
            SignInScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("chat/{channelId}", arguments = listOf(
            navArgument("channelId"){
                type = NavType.StringType
            }
        )) {
            val channelId = it.arguments?.getString("channelId") ?: ""
            ChatScreen(navController, channelId)
        }
    }
}
}