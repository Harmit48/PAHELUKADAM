package com.example.pahelukadam

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appNameText: TextView = findViewById(R.id.appName)
        val signInBtn: Button = findViewById(R.id.signInBtn)
        val signUpBtn: Button = findViewById(R.id.signUpBtn)
        val emailField: TextInputEditText = findViewById(R.id.emailField)
        val passwordField: TextInputEditText = findViewById(R.id.passwordField)

        // Apply custom font
        val typeface = ResourcesCompat.getFont(this, R.font.major_mono_display)
        appNameText.typeface = typeface

        // ✅ Sign In Button Click with Validation
        signInBtn.setOnClickListener {
            val emailOrPhone = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (emailOrPhone.isEmpty()) {
                emailField.error = "Please enter Email or Phone"
                emailField.requestFocus()
                return@setOnClickListener
            }

            // Check if input is valid Email OR Phone Number
            if (!Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches() &&
                !Patterns.PHONE.matcher(emailOrPhone).matches()
            ) {
                emailField.error = "Enter valid Email or Phone"
                emailField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordField.error = "Password required"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordField.error = "Password must be at least 6 characters"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            // ✅ If valid, proceed with login logic (API / Database check)
            Toast.makeText(this, "Validation Passed ✅", Toast.LENGTH_SHORT).show()

            // Example: move to HomeActivity after login success
            // val intent = Intent(this, HomeActivity::class.java)
            // startActivity(intent)
        }

        // Sign Up Button click -> Open SignUpActivity
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
