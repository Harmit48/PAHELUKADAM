package com.pahekukadam.pahelukadam

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pahekukadam.pahelukadam.ui.HubActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var animatedText: TextView
    private val words = arrayOf("Innovation", "Startup", "Entrepreneurship")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animatedText = findViewById(R.id.animatedText)

        // Your animation logic (no changes needed here)
        for (i in words.indices) {
            Handler(Looper.getMainLooper()).postDelayed({
                animatedText.text = words[i]
                applyGradient(animatedText)
            }, (i * 1000).toLong())
        }

        // ✅ MODIFIED NAVIGATION LOGIC
        // After 3 seconds, check login status and navigate accordingly
        Handler(Looper.getMainLooper()).postDelayed({
            // Get SharedPreferences
            val sharedPref: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

            // Decide which activity to start based on login status
            val intent = if (isLoggedIn) {
                // If logged in, go to the main HubActivity
                Intent(this, HubActivity::class.java)
            } else {
                // If not logged in, go to the MainActivity (login screen)
                Intent(this, MainActivity::class.java)
            }

            startActivity(intent)
            finish() // Close SplashActivity so the user can't navigate back to it
        }, 3000)
    }

    private fun applyGradient(textView: TextView) {
        textView.post {
            val paint = textView.paint
            val width = paint.measureText(textView.text.toString())
            val textShader: Shader = LinearGradient(
                0f, 0f, width, textView.textSize,
                intArrayOf(
                    Color.parseColor("#F48C06"),
                    Color.parseColor("#DC2F02")
                ),
                null, Shader.TileMode.CLAMP
            )
            textView.paint.shader = textShader
            textView.invalidate()
        }
    }
}