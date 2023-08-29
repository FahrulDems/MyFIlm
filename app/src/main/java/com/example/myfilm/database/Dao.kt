package com.example.myfilm.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user where email = :email")
    suspend fun getUserByEmail(email: String): User

    @Query("SELECT * FROM user where isLogin = :isLogin")
    suspend fun getUserByLogin(isLogin: Boolean): User

    @Query("UPDATE user SET isLogin = :isLogin where id = :id")
    suspend fun updateUser(id: Int, isLogin: Boolean)
}

@Dao
interface  MovieDao{
    @Insert
    suspend fun insertMovieFav(movieFav: MovieEntity)

    @Query("SELECT * FROM movies_fav WHERE user_id = :userId")
    suspend fun getAllByUserId(userId: Int): List<MovieEntity>?

    @Query("DELETE FROM movies_fav WHERE movie_id = :movieId")
    suspend fun deleteMovieFav(movieId: Int)
}
