package com.example.myfilm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfilm.screen.DetailScreen
import com.example.myfilm.screen.FavoritesScreen
import com.example.myfilm.screen.HomeScreen
import com.example.myfilm.screen.LoginScreen
import com.example.myfilm.screen.PreLoginScreen
import com.example.myfilm.screen.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "prelogin")
    {
        composable("prelogin"){
            PreLoginScreen(navController)
        }
        composable("login") {
            LoginScreen(onLoginButtonClicked = { navController.navigate("home")},
                        onRegisterButtonClicked = {navController.navigate("register")})
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home"){
            HomeScreen(navController = navController)
        }
        composable("favorites"){
            FavoritesScreen(navController = navController)
        }
        composable("detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            movieId?.let {
                DetailScreen(navController = navController, itemId = movieId)
            }
        }
    }
}