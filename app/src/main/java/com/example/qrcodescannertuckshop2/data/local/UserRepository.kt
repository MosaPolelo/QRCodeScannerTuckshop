package com.example.qrcodescannertuckshop2.data.local

import com.example.qrcodescannertuckshop2.AppUser
import com.example.qrcodescannertuckshop2.UserRole

/**
 * Abstraction for local user storage.
 * Implemented by LocalUserRepository.
 */
interface UserRepository {
    suspend fun ensureAdminSeeded(): AppUser
    suspend fun addUser(
        username: String,
        role: UserRole,
        pin: String,
        photoPath: String?
    ): Result<Unit>

    suspend fun list(): List<UserEntity>
    suspend fun delete(username: String)
    suspend fun changeRole(username: String, role: UserRole)
    suspend fun resetPin(username: String, newPin: String)

    suspend fun verify(username: String, pin: String): AppUser?
    suspend fun updatePhoto(username: String, photoPath: String?)

}
