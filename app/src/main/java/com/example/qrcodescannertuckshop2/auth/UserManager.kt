package com.example.qrcodescannertuckshop2.auth

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.FileProvider
import com.example.qrcodescannertuckshop2.data.local.ProfileStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object UserManager {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Firebase user state
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    // Local photo path (persisted in DataStore)
    private val _localPhotoPath = MutableStateFlow<String?>(null)
    val localPhotoPath = _localPhotoPath.asStateFlow()

    private var listener: FirebaseAuth.AuthStateListener? = null

    fun start() {
        if (listener != null) return
        listener = FirebaseAuth.AuthStateListener { fb -> _currentUser.value = fb.currentUser }
        auth.addAuthStateListener(listener!!)
    }

    fun onGoogleSignIn(user: FirebaseUser?) {
        _currentUser.value = user
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }

// inside object UserManager

    suspend fun refreshLocalPhoto(context: Context) {
        val saved: String? = ProfileStore.photoPathFlow(context) // flow itself
            .let { flow ->
                // you can keep this as is if we don't actually call it yet
                null
            }
    }
// inside object UserManager


    // inside object UserManager

    // UserManager.kt
// UserManager.kt
    suspend fun updatePhoto(
        context: Context,
        uid: String,
        bitmap: Bitmap
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }

            // save per-user path in DataStore
            ProfileStore.savePhotoPath(context, uid, file.absolutePath)

            // try Firebase photo update if a Firebase user exists (best-effort)
            auth.currentUser?.let { user ->
                runCatching {
                    val uri = FileProvider.getUriForFile(
                        context, "${context.packageName}.provider", file
                    )
                    val req = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                    user.updateProfile(req).await()
                }
            }

            true
        } catch (e: Exception) {
            Log.e("UserManager", "updatePhoto: error", e)
            false
        }
    }






}
