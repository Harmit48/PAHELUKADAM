package com.pahelukadam.pahelukadam

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.pahelukadam.pahelukadam.ui.HubActivity // ⬅️ HubActivity

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

        // ✅ Sign In Button Click with Validation + Save to SharedPreferences
        signInBtn.setOnClickListener {
            val emailOrPhone = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

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

            // ✅ Save user data into SharedPreferences
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            sharedPref.edit()
                .putString("USER_EMAIL", emailOrPhone)
                .putString("USER_PASSWORD", password) // ⚠️ Not secure, just demo
                .apply()

            // ✅ If valid, redirect to HubActivity
            val intent = Intent(this, HubActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Sign Up Button click -> Open SignUpActivity
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
