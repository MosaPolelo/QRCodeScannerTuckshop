package com.example.qrcodescannertuckshop2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // 👇 Start a coroutine for delay + navigation
        lifecycleScope.launch {
            delay(2000) // Wait for 2 seconds
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Finish splash so user can't return to it
        }
    }
}
