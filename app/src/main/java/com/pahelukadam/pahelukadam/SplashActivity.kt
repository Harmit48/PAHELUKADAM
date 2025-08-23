package com.pahelukadam.pahelukadam

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var animatedText: TextView
    private val words = arrayOf("Innovation", "Startup", "Entrepreneurship")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animatedText = findViewById(R.id.animatedText)

        // Show each word with 1s delay
        for (i in words.indices) {
            Handler(Looper.getMainLooper()).postDelayed({
                animatedText.text = words[i]
                applyGradient(animatedText)
            }, (i * 1000).toLong())
        }

        // After 3s -> go to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close SplashActivity
        }, 3000)
    }

    // Apply gradient color to text
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
