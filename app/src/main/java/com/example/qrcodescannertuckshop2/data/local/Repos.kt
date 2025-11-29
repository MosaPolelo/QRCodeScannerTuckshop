package com.example.qrcodescannertuckshop2.data.local

import android.content.Context

object Repos {

    @Volatile
    private var _userRepo: UserRepository? = null

    /**
     * Singleton accessor for the local user repository.
     * Use this from Compose / activities: Repos.userRepo(context)
     */
    fun userRepo(context: Context): UserRepository {
        return _userRepo ?: synchronized(this) {
            _userRepo ?: LocalUserRepository(
                AppDb.getInstance(context.applicationContext).userDao()
            ).also { created ->
                _userRepo = created
            }
        }
    }
}
