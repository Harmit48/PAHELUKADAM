package com.example.pahelukadam

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appNameText: TextView = findViewById(R.id.appName)
        val signInBtn: Button = findViewById(R.id.signInBtn)
        val signUpBtn: Button = findViewById(R.id.signUpBtn)

        // Apply custom font
        val typeface = ResourcesCompat.getFont(this, R.font.major_mono_display)
        appNameText.typeface = typeface

        // Dark Gradient text effect (applied after layout)
        appNameText.viewTreeObserver.addOnGlobalLayoutListener {
            val textWidth = appNameText.paint.measureText(appNameText.text.toString())
            val textShader = LinearGradient(
                0f, 0f, textWidth, appNameText.textSize,   // horizontal gradient
                intArrayOf(
                    android.graphics.Color.parseColor("#F48C06"), // Dark Red
                    android.graphics.Color.parseColor("#DC2F02"), // Orange Red
                   // android.graphics.Color.parseColor("#000000")  // Black
                ),
                null,
                Shader.TileMode.CLAMP
            )
            appNameText.paint.shader = textShader
            appNameText.invalidate()
        }

        // Sign In Button click
        signInBtn.setOnClickListener {
            // TODO: Implement login logic
        }

        // Sign Up Button click -> Open SignUpActivity
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
