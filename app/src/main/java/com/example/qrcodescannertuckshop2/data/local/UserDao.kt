package com.example.qrcodescannertuckshop2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun find(username: String): UserEntity?

    // NEW: find by uid when needed
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    suspend fun findByUid(uid: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserEntity)

    @Update
    suspend fun update(entity: UserEntity)

    @Delete
    suspend fun delete(entity: UserEntity)
}
