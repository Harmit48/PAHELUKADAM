package com.example.pahelukadam

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.pahelukadam.ui.HubActivity
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

            // Input validations
            if (emailOrPhone.isEmpty()) {
                emailField.error = "Please enter Email or Phone"
                emailField.requestFocus()
                return@setOnClickListener
            }

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

            // ✅ Fetch stored credentials
            val sharedPref: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedEmail = sharedPref.getString("email", null)
            val savedPassword = sharedPref.getString("password", null)

            if (savedEmail == null || savedPassword == null) {
                // No user registered yet
                Toast.makeText(this, "No account found. Please sign up first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Match login
            if (emailOrPhone == savedEmail && password == savedPassword) {
                val editor = sharedPref.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                // Go to HubActivity
                val intent = Intent(this, HubActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email/phone or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Sign Up Button click -> Open SignUpActivity
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
