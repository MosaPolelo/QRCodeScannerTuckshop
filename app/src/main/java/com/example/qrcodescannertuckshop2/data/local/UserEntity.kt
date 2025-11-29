package com.example.qrcodescannertuckshop2.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,

    // NEW: real uid stored in Room (unique per user)
    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "role")
    val role: String,

    @ColumnInfo(name = "pinHash")
    val pinHash: String,

    @ColumnInfo(name = "salt")
    val salt: String,

    @ColumnInfo(name = "photoPath")
    val photoPath: String? = null
)
