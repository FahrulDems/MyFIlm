package com.example.myfilm.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myfilm.data.Movie
import com.example.myfilm.data.MovieResponse
import com.example.myfilm.data.authorozationHeader
import com.example.myfilm.data.movieApiService
import com.example.myfilm.database.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var popularMovieList by remember { mutableStateOf(emptyList<Movie>()) }
    var topRatedMovieList by remember { mutableStateOf(emptyList<Movie>()) }
    var nowPlayingMovieList by remember { mutableStateOf(emptyList<Movie>()) }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val response: MovieResponse = movieApiService.getPopularMovies(
                    authorozationHeader
                )
                popularMovieList = response.results ?: emptyList()
            } catch (e: Exception) {
                Log.e("MovieDBScreen", "Error fetching top rated movies: ${e.message}", e)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val response: MovieResponse = movieApiService.getTopRatedMovies(
                    authorozationHeader
                )
                topRatedMovieList = response.results ?: emptyList()
            } catch (e: Exception) {
                Log.e("MovieDBScreen", "Error fetching top rated movies: ${e.message}", e)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val response: MovieResponse = movieApiService.getNowPlayingMovies(
                    authorozationHeader
                )
                nowPlayingMovieList = response.results ?: emptyList()
            } catch (e: Exception) {
                Log.e("MovieDBScreen", "Error fetching top rated movies: ${e.message}", e)
            }
        }
    }

    var selectedTab by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp)
        ) {
            LazyColumn {
                item {
                    TopScreen(navController, popularMovieList)
                }
                item {
                    MiddleScreen(navController, topRatedMovieList)
                }
                item {
                    BottomScreen(navController, nowPlayingMovieList)
                }
            }
        }

        BottomBar(
            selectedTab = selectedTab,
            onLogoutClicked = {
                navController.navigate("prelogin") {
                    popUpTo("prelogin") { inclusive = true }
                }
            }) { newIndex ->
            if (newIndex == selectedTab) {
                navController.popBackStack()
            } else {
                selectedTab = newIndex
                navigateToScreen(navController, newIndex)
            }
        }
    }
}

private fun navigateToScreen(navController: NavController, index: Int) {
    when (index) {
        0 -> navController.navigate("home")
        1 -> navController.navigate("favorites")
    }
}

@Composable
fun BottomBar(selectedTab: Int, onLogoutClicked: () -> Unit, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 784.dp)
            .height(60.dp)
            .background(Color(0xFF34806e)),
        horizontalArrangement = Arrangement.Center
    ) {
        BottomBarItem(
            icon = Icons.Default.Home,
            label = "Home",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        BottomBarItem(
            icon = Icons.Default.Favorite,
            label = "Favorites",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        val context = LocalContext.current
        val userDao = (context.applicationContext as MyApplication).database.userDao()
        BottomBarItem(
            icon = Icons.Default.Logout,
            label = "Logout",
            isSelected = false,
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userDao.getUserByLogin(true)
                    user?.let { nonNullUser ->
                        withContext(Dispatchers.Main) {
                            userDao.updateUser(nonNullUser.id, false)
                            onLogoutClicked()
                            Toast.makeText(context, "You are logout now", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun NullOrNotInternetConnection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading",
            color = Color(0xFF7CE7AC),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(60.dp)
            .width(60.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (!isSelected) {
                        onClick()
                    }
                }
            )
            .padding(top = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 12.sp
        )
    }
}


@Composable
fun TopScreen(navController: NavController, popularMovieList: List<Movie>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(Color.Transparent)
    ) {
        userLogin?.let {
            Text(
                text = "Hey Welcome, ${it.name}",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
        }
        Text(
            text = "Popular Movies",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp, bottom = 2.dp)
        )
        if (popularMovieList.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(popularMovieList) { movie ->
                    MovieItem(movie, onItemClick = {
                        navController.navigate("detail/${movie.id}")
                    })
                }
            }
        } else {
            NullOrNotInternetConnection()
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onItemClick: (String) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.poster_path}"
    Card(
        border = BorderStroke(2.dp, Color.Yellow),
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .clickable(onClick = {
                onItemClick(movie.id.toString())
            }),
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun MiddleScreen(navController: NavController, movieList: List<Movie>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Transparent)
    ) {
        Text(
            text = "Top Rated Movies",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        if (movieList.isNotEmpty()) {
            CardsMid(navController, movieList)
        }else{
            NullOrNotInternetConnection()
        }
    }
}

@Composable
fun CardsMid(navController: NavController, movieList: List<Movie>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(movieList) { movie ->
            val cardData = CardData(movie.title, movie.poster_path, "Description:${movie.title}")
            CardItem(cardData) {
                navController.navigate("detail/${movie.id}")
            }
        }
    }
}

@Composable
fun BottomScreen(
    navController: NavController,
    nowPlayingMovieList: List<Movie>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 2.dp)
        ) {
            Text(
                text = "Now Playing Movies",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (nowPlayingMovieList.isNotEmpty()){
                CardsBottom(navController, nowPlayingMovieList)
            }else{
                NullOrNotInternetConnection()
            }
        }
    }
}

@Composable
fun CardsBottom(navController: NavController, movieList: List<Movie>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(movieList) { movie ->
            val cardData = CardData(movie.title, movie.poster_path, "Description:${movie.title}")
            CardItem(cardData) {
                navController.navigate("detail/${movie.id}")
            }
        }
    }
}

data class CardData(val title: String, val image: String, val description: String)

@Composable
fun CardItem(cardData: CardData, onClick: () -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.Yellow),
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500/${cardData.image}"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

