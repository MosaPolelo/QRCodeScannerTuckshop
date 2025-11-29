package com.example.qrcodescannertuckshop2.data.local

import com.example.qrcodescannertuckshop2.AppUser
import com.example.qrcodescannertuckshop2.UserRole
import java.security.MessageDigest

class LocalUserRepository(
    private val dao: UserDao
) : UserRepository {

    /** Make sure we always have at least one admin user on a clean DB */
    override suspend fun ensureAdminSeeded(): AppUser {
        if (dao.getAll().isEmpty()) {
            val username = "Mosa"
            val role = UserRole.ADMIN
            val pin = "1234"
            val salt = java.util.UUID.randomUUID().toString()
            val hash = hashPin(pin, salt)
            val uid = "Local:$username"

            dao.insert(
                UserEntity(
                    uid = uid,
                    username = username,
                    role = role.name,
                    pinHash = hash,
                    salt = salt,
                    photoPath = null
                )
            )
        }

        val u: UserEntity? = dao.find("Mosa")
        return if (u != null) {
            AppUser(
                uid = u.uid,
                username = u.username,
                role = UserRole.valueOf(u.role),
                photoPath = u.photoPath
            )
        } else {
            // Shouldn't happen, but keep the app resilient
            AppUser(
                uid = "Local:Mosa",
                username = "Mosa",
                role = UserRole.ADMIN,
                photoPath = null
            )
        }
    }

    /** Update the stored photo path (by username for now) */
    override suspend fun updatePhoto(username: String, photoPath: String?) {
        val existing = dao.find(username) ?: return
        dao.update(existing.copy(photoPath = photoPath))
    }

    /** Add a new local user (creates a stable Local:<username> uid) */
    override suspend fun addUser(
        username: String,
        role: UserRole,
        pin: String,
        photoPath: String?
    ): Result<Unit> {
        val existing = dao.find(username)
        if (existing != null) {
            return Result.failure(IllegalStateException("User already exists"))
        }

        val uid = "Local:$username"
        val salt = java.util.UUID.randomUUID().toString()
        val hash = hashPin(pin, salt)

        dao.insert(
            UserEntity(
                uid = uid,
                username = username,
                role = role.name,
                pinHash = hash,
                salt = salt,
                photoPath = photoPath
            )
        )
        return Result.success(Unit)
    }

    override suspend fun list(): List<UserEntity> = dao.getAll()

    override suspend fun delete(username: String) {
        dao.find(username)?.let { dao.delete(it) }
    }

    override suspend fun changeRole(username: String, role: UserRole) {
        val u = dao.find(username) ?: return
        dao.update(u.copy(role = role.name))
    }

    override suspend fun resetPin(username: String, newPin: String) {
        val u = dao.find(username) ?: return
        val hash = hashPin(newPin, u.salt)
        dao.update(u.copy(pinHash = hash))
    }

    override suspend fun verify(username: String, pin: String): AppUser? {
        val u = dao.find(username) ?: return null
        return if (hashPin(pin, u.salt) == u.pinHash) {
            AppUser(
                uid = u.uid,
                username = u.username,
                role = UserRole.valueOf(u.role),
                photoPath = u.photoPath
            )
        } else null
    }

    private fun hashPin(pin: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest("$salt:$pin".toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
