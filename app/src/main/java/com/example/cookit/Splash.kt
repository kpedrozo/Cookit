package com.example.cookit

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        progressBar.max = 1000

        val currentProgress = 1000

        ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
            .setDuration(4000)
            .start()

    }

    override fun onStart() {
        super.onStart()
        Log.d("Splash", "onStart")

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("Splash", "post Delayed")
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        },4000);
    }
}