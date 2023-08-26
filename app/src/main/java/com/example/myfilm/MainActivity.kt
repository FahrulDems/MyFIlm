package com.example.myfilm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myfilm.navigation.AppNavigation
import com.example.myfilm.ui.theme.MyFIlmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFIlmTheme {
                AppNavigation()
            }
        }
    }
}