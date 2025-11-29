package com.example.qrcodescannertuckshop2

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1) Backing store
private val Context.userPrefs by preferencesDataStore(name = "user_prefs")

// 2) Keys
private object UserPrefsKeys {
    val USERNAME  = stringPreferencesKey("username")
    val ROLE      = stringPreferencesKey("role")
    val PHOTOPATH = stringPreferencesKey("photo_path")
}

// 3) Helper
class UserDataStore(private val context: Context) {

    // Read user as a Flow (null if nothing saved yet)
// UserDataStore.kt

    val userFlow: Flow<AppUser?> =
        context.userPrefs.data.map { prefs ->
            val username: String? = prefs[UserPrefsKeys.USERNAME]
            val roleStr: String?   = prefs[UserPrefsKeys.ROLE]
            val photoPath: String? = prefs[UserPrefsKeys.PHOTOPATH]

            if (username != null && roleStr != null) {
                val role: UserRole = runCatching { UserRole.valueOf(roleStr) }
                    .getOrElse { UserRole.CASHIER }

                // NEW: provide a stable local uid
                val uid = "local:$username"

                AppUser(
                    uid = uid,
                    username = username,
                    role = role,
                    photoPath = photoPath
                )
            } else {
                null
            }
        }


    // Save/overwrite the current user
    suspend fun saveUser(user: AppUser) {
        context.userPrefs.edit { prefs ->
            prefs[UserPrefsKeys.USERNAME]  = user.username
            prefs[UserPrefsKeys.ROLE]      = user.role.name
            if (user.photoPath != null) {
                prefs[UserPrefsKeys.PHOTOPATH] = user.photoPath!!
            } else {
                prefs.remove(UserPrefsKeys.PHOTOPATH)
            }
        }
    }

    // Clear (useful for logout)
    suspend fun clearUser() {
        context.userPrefs.edit { it.clear() }
    }
}
