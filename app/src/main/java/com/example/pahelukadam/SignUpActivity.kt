package com.example.pahelukadam

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Gradient for App Name
        val appName: TextView = findViewById(R.id.appName)
        val paint = appName.paint
        val width = paint.measureText(appName.text.toString())
        val textShader = LinearGradient(
            0f, 0f, width, appName.textSize,
            intArrayOf(
                android.graphics.Color.parseColor("#F48C06"),
                android.graphics.Color.parseColor("#DC2F02")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        appName.paint.shader = textShader

        // Sign in button (actually TextView now)
        val signInBtn: TextView = findViewById(R.id.signInBtn)

        // Redirect to Sign In (MainActivity)
        signInBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // close signup page
        }
    }
}
