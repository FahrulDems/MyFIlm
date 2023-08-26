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
}