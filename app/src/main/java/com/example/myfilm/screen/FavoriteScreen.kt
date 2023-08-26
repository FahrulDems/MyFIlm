package com.example.myfilm.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(navController: NavController/*, favoriteMovies: List<Movie>*/) {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            LazyColumn {
            }
        }
        BottomBar(selectedTab = 1, onLogoutClicked = {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }) { newIndex ->
            when (newIndex) {
                0 -> navController.navigate("home")
                1 -> navController.navigate("favorites")
            }
        }
    }
}
