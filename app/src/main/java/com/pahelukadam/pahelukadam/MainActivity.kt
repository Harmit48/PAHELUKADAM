package com.example.pahelukadam

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pahelukadam.ui.HubActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise SharedPreferences
        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // ✅ Gradient for App Name
        val appName: TextView = findViewById(R.id.appName)
        val paint = appName.paint
        val width = paint.measureText(appName.text.toString())
        val textShader = LinearGradient(
            0f, 0f, width, appName.textSize,
            intArrayOf(
                android.graphics.Color.parseColor("#F48C06"), // orange
                android.graphics.Color.parseColor("#DC2F02")  // red
            ),
            null,
            Shader.TileMode.CLAMP
        )
        appName.paint.shader = textShader

        // ✅ Auto-login if already signed in
        if (sharedPref.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this, HubActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val signInBtn: Button = findViewById(R.id.signInBtn)
        val signUpBtn: Button = findViewById(R.id.signUpBtn)
        val emailField: TextInputEditText = findViewById(R.id.emailField)
        val passwordField: TextInputEditText = findViewById(R.id.passwordField)

        signInBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // Basic validation
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve saved credentials
            val savedEmail = sharedPref.getString("email", null)
            val savedPassword = sharedPref.getString("password", null)

            if (email == savedEmail && password == savedPassword) {
                // ✅ Save login state
                sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                // Navigate to HubActivity
                val intent = Intent(this, HubActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
