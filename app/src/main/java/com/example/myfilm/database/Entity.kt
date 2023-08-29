package com.example.myfilm.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean = false
)

@Entity(tableName = "movies_fav")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val movie_id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val release_date: String,
    val user_id: Int,
    val vote_average: Double,
    val vote_count: Int
)