package com.example.myfilm.screen

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myfilm.data.Movie
import com.example.myfilm.data.MovieResponse
import com.example.myfilm.data.authorozationHeader
import com.example.myfilm.data.movieApiService
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var popularMovieList by remember {mutableStateOf(emptyList<Movie>())}
    var topRatedMovieList by remember {mutableStateOf(emptyList<Movie>())}
    var nowPlayingMovieList by remember {mutableStateOf(emptyList<Movie>())}

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
                .padding(bottom = 70.dp)
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

        // Place the BottomBar at the bottom of the screen
        BottomBar(
            selectedTab = selectedTab,
            onLogoutClicked = {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
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
            .padding(top =765.dp)
            .height(70.dp)
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
        BottomBarItem(
            icon = Icons.Default.Logout,
            label = "Logout",
            isSelected = false,
            onClick = { onLogoutClicked() }
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
            .padding(top = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Black else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp
        )
    }
}


@Composable
fun TopScreen(navController: NavController, popularMovieList: List<Movie>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(Color.Transparent)
    ) {
        Text(
            text = "Popular Movies",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Adjust the spacing as needed
        ) {
            items(popularMovieList) { movie ->
                MovieItem(movie, onItemClick = {
                    navController.navigate("detail/${movie.id}")
                })
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onItemClick: (String) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.poster_path}"
    Box(
        modifier = Modifier
            .width(150.dp) // Increase the width for larger images
            .fillMaxHeight()
            //.padding(start = 16.dp, end = 8.dp)
            .clickable(onClick = {
                onItemClick(movie.id.toString())
            }),
        contentAlignment = Alignment.Center
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
            .height(240.dp)
            .background(Color.Transparent)
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Top Rated Movies",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        CardsMid(navController, movieList)
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
fun BottomScreen(navController: NavController, nowPlayingMovieList: List<Movie>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Now Playing Movies",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp)
            )

            // Use Spacer to push the following content to the bottom
            Spacer(modifier = Modifier.weight(1f))
            CardsBottom(navController, nowPlayingMovieList)
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
        border = BorderStroke(2.dp, Color.Gray),
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                //.padding(8.dp)
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500/${cardData.image}"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cardData.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

/*@Preview
@Composable
fun PreviewRegisterScreen() {
    MyFIlmTheme {
        HomeScreen(navController = rememberNavController())
    }
}*/

