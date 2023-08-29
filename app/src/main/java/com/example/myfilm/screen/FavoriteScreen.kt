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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myfilm.database.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FavoritesScreen(navController: NavController) {
    val context = LocalContext.current
    val movieDao = (context.applicationContext as MyApplication).database.movieDao()
    var movieList by remember {mutableStateOf<List<com.example.myfilm.data.Movie>?>(null)}

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
                    vote_average = movieFav.vote_average,
                    vote_count = movieFav.vote_count
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
            Text(
                text = "Favorite Movies",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn()
                    .padding(top = 8.dp),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.SansSerif
                ),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                if (movieList.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorite Empty",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .padding(8.dp),
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Favorite Empty",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.Gray // Set the text color to gray
                                    )
                                )
                            }
                        }
                    }

                } else {
                    items(movieList!!) { movie ->
                        ItemCard(
                            movie = movie,
                            navController = navController
                        )
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
fun ItemCard(movie: com.example.myfilm.data.Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("detail/${movie.id}") },
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(90.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500" + movie.poster_path),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        //.padding(end = 16.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Text(
                    text = "Title: ${movie.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Release Date: ${movie.release_date}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Vote Average: ${movie.vote_average}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}
