package com.example.pahelukadam

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appNameText: TextView = findViewById(R.id.appName)

        // Apply custom font
        val typeface = ResourcesCompat.getFont(this, R.font.major_mono_display)
        appNameText.typeface = typeface

        // Wait until layout is drawn to apply gradient correctly
        appNameText.post {
            val textShader = LinearGradient(
                0f, 0f,
                appNameText.paint.measureText(appNameText.text.toString()), 0f,
                intArrayOf(
                    Color.parseColor("#DC2F02"), // Start color
                    Color.parseColor("#F48C06")  // End color
                ),
                null,
                Shader.TileMode.CLAMP
            )
            appNameText.paint.shader = textShader
            appNameText.invalidate()
        }
    }
}
