package com.example.mirmessager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mirmessager.ui.theme.MIRMessagerTheme
import dagger.hilt.android.AndroidEntryPoint


//зачем тут эта аннотация и что она делает? upd все еще до конца не понял
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MIRMessagerTheme {
               MainApp()

            }
        }
    }
}

@Preview
@Composable
fun showUi(){
    MaterialTheme {
        MainApp()
    }
}