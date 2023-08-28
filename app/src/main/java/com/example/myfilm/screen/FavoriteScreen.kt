package com.example.myfilm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myfilm.appcomponent.HeadingTextComponent
import com.example.myfilm.database.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current
    val movieDao = (context.applicationContext as MyApplication).database.movieDao()
    val userDao = (context.applicationContext as MyApplication).database.userDao()

    var movieList by remember {
        mutableStateOf<List<com.example.myfilm.data.Movie>?>(null)
    }

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieFavList = movieDao.getAllByUserId(userLogin!!.id)
            val mappedMovieList = movieFavList?.map { movieFav ->
                com.example.myfilm.data.Movie(
                    id = movieFav.movie_id,
                    title = movieFav.title,
                    overview = movieFav.overview,
                    poster_path = movieFav.poster_path,
                    release_date = movieFav.release_date,
                    vote_average = 0.1,
                    vote_count = 1
                )
            }
            withContext(Dispatchers.Main) {
                movieList = mappedMovieList
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp)
        ) {
            HeadingTextComponent(value = "Favori")
            Spacer(modifier = Modifier.heightIn(min = 10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                items(movieList ?: emptyList()) { movie ->
                    ItemCard(movie = movie){
                        navController.navigate("detail/${movie.id}")
                    }
                }
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

@Composable
fun ItemCard(movie: com.example.myfilm.data.Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onClick },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500" + movie.poster_path),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Column {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}