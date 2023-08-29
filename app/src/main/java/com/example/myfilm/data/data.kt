package com.example.myfilm.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class MovieResponse(
    val results: List<Movie>?
)
data class Genre(val name: String)
data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val vote_average: Double,
    val release_date: String,
    val vote_count: Int,
    val genres: List<Genre>? = null
)

interface MovieApiService {
    @GET("movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(@Header("Authorization") authorozation: String): MovieResponse

    @GET("movie/top_rated?language=en-US&page=2")
    suspend fun getTopRatedMovies(@Header("Authorization") authorozation: String): MovieResponse

    @GET("movie/now_playing?language=en-USpage=1")
    suspend fun getNowPlayingMovies(@Header("Authorization") authorozation: String): MovieResponse

    @GET("movie/{movie_id}?language=en-US")
    suspend fun getDetailMovies(@Header("Authorization") authorozation: String,
                                @Path("movie_id") movieId: String): Movie
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()
}

val movieApiService: MovieApiService = RetrofitInstance.retrofit.create(MovieApiService::class.java)
const val authorozationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NTk3YTA0ZDMwMTJkMzJkMGQ4NGQwMmQyMjRmNGNhZSIsInN1YiI6IjY0ZTVkOThkZDIzNmU2MDEzYjMxOTMxMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.to6NIhDs8fTdJMbLuEJ9j3BcJpBpAjCWMf_Y7lmA2Ag"