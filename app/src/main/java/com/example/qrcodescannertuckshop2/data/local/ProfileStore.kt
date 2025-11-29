package com.example.qrcodescannertuckshop2.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException


// Single DataStore instance for the profile
private val Context.profileDataStore by preferencesDataStore(name = "profile_store")

object ProfileStore {

    // Keys
    private val KEY_ACTIVE_NAME = stringPreferencesKey("active_name")
    private val KEY_ACTIVE_ROLE = stringPreferencesKey("active_role")
    private val KEY_PHOTO_PATH  = stringPreferencesKey("photo_path")

    // ---------------------------------------------------------------------
    // OPTIONAL FLOWS (nice for future use with collectAsState).
    // They are not required for what you’re doing right now, but harmless.
    // ---------------------------------------------------------------------

    fun nameFlow(ctx: Context): Flow<String?> =
        ctx.profileDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs -> prefs[KEY_ACTIVE_NAME] }

    fun roleFlow(ctx: Context): Flow<String?> =
        ctx.profileDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs -> prefs[KEY_ACTIVE_ROLE] }

    fun photoPathFlow(ctx: Context): Flow<String?> =
        ctx.profileDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs -> prefs[KEY_PHOTO_PATH] }

    // ---------------------------------------------------------------------
    // ONE-SHOT GETTERS (used with remember { … } in Composables)
    // These are what your current TuckshopTopBar / SettingsScreen want.
    // ---------------------------------------------------------------------

    fun getActiveName(ctx: Context): String? =
        runBlocking {
            ctx.profileDataStore.data.first()[KEY_ACTIVE_NAME]
        }

    fun getActiveRole(ctx: Context): String? =
        runBlocking {
            ctx.profileDataStore.data.first()[KEY_ACTIVE_ROLE]
        }

    fun getPhotoPath(ctx: Context, uid: String): String? =
        runBlocking {
            ctx.profileDataStore.data.first()[stringPreferencesKey("KEY_PHOTO_PATH_UID_$uid")]

        }

    suspend fun clearPhoto(context: Context) {
        context.profileDataStore.edit {
            it.remove(KEY_PHOTO_PATH)
        }
    }


    // ---------------------------------------------------------------------
    // WRITERS
    // ---------------------------------------------------------------------

    // NEW: this is what handleCredentialResponse() calls
    suspend fun saveActive(
        ctx: Context,
        name: String,
        role: String,
        photoPath: String? = null
    ) {
        ctx.profileDataStore.edit { prefs ->
            prefs[KEY_ACTIVE_NAME] = name
            prefs[KEY_ACTIVE_ROLE] = role
            if (photoPath != null) {
                prefs[KEY_PHOTO_PATH] = photoPath
            }
        }
    }


    suspend fun savePhotoPath(ctx: Context, uid: String, path: String?) {
        ctx.profileDataStore.edit { prefs ->
            if (path != null) {
                prefs[stringPreferencesKey("KEY_PHOTO_PATH_UID_$uid")] = path
                Log.d("ProfileStore", "savePhotoPath uid=$uid path=$path")
            }
        }
    }

}
