package com.example.myfilm.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myfilm.data.Movie
import com.example.myfilm.data.authorozationHeader
import com.example.myfilm.data.movieApiService
import com.example.myfilm.database.MovieEntity
import com.example.myfilm.database.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DetailScreen(navController: NavController, itemId: String) {
    val context = LocalContext.current
    val movieDetail = remember { mutableStateOf<Movie?>(null) }
    val isFavorite = remember { mutableStateOf(false) }
    val movieDao = (context.applicationContext as MyApplication).database.movieDao()
    var movieList by remember { mutableStateOf<List<MovieEntity>?>(null) }

    LaunchedEffect(key1 = true) {
        val response = movieApiService.getDetailMovies(authorozationHeader, itemId)
        movieDetail.value = response

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                movieList = userLogin?.let { movieDao.getAllByUserId(it.id) }

                val isMovieInFavoriteList = movieList?.any { movieFav ->
                    movieFav.movie_id == (movieDetail.value?.id ?: 0)
                } ?: false

                if (isMovieInFavoriteList) {
                    isFavorite.value = true
                }
            }
        }
    }

    val movie = movieDetail.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE9F1EF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                movie?.let {
                    Text(
                        text = "Back To Home",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                movie?.let {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        IconButton(
                            onClick = {
                                if (!isFavorite.value) {
                                    val userDao =
                                        (context.applicationContext as MyApplication).database.userDao()

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val user = userDao.getUserByLogin(true)
                                        user.let { nonNullUser ->
                                            val movieEntity = MovieEntity(
                                                movie_id = movie.id,
                                                title = movie.title,
                                                poster_path = movie.poster_path,
                                                overview = movie.overview,
                                                release_date = movie.release_date,
                                                user_id = nonNullUser.id
                                            )
                                            movieDao.insertMovieFav(movieEntity)
                                        }
                                    }

                                    isFavorite.value = true

                                    Toast.makeText(context, "Add to your favorite", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, "This movie is already in your favorite list", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            }
                        ) {
                            Icon(
                                imageVector =   Icons.Default.Favorite,
                                contentDescription = "Add to favorite",
                                tint = if (isFavorite.value) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            movie?.let { it ->
                val imageUrl = "https://image.tmdb.org/t/p/w500/${it.poster_path}"
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = it.title,
                    modifier = Modifier
                        .size(500.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "Genre: ${movie.genres?.joinToString(", ") { it.name }}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Release Date: ${it.release_date}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Vote Average: ${it.vote_count}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Vote Average: ${it.vote_average}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = it.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
