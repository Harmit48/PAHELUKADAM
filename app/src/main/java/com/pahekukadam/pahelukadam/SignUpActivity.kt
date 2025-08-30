package com.pahekukadam.pahelukadam

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
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)  // ✅ Make sure your XML file name matches

        // 🌈 Gradient for App Name
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

        // 📝 Input fields
        val firstNameEt: TextInputEditText = findViewById(R.id.firstName)
        val lastNameEt: TextInputEditText = findViewById(R.id.lastName)
        val emailEt: TextInputEditText = findViewById(R.id.email)
        val passwordEt: TextInputEditText = findViewById(R.id.password)
        val confirmPasswordEt: TextInputEditText = findViewById(R.id.confirmPassword)

        // 🔘 Buttons
        val registerBtn: Button = findViewById(R.id.registerBtn)
        val signInBtn: TextView = findViewById(R.id.signInBtn)

        // ✅ Register Button Logic
        registerBtn.setOnClickListener {
            val firstName = firstNameEt.text.toString().trim()
            val lastName = lastNameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString()
            val confirmPassword = confirmPasswordEt.text.toString()

            when {
                firstName.isEmpty() -> {
                    firstNameEt.error = "First Name cannot be blank"
                    firstNameEt.requestFocus()
                }
                lastName.isEmpty() -> {
                    lastNameEt.error = "Last Name cannot be blank"
                    lastNameEt.requestFocus()
                }
                email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailEt.error = "Enter a valid Email ID"
                    emailEt.requestFocus()
                }
                password.isEmpty() -> {
                    passwordEt.error = "Password cannot be blank"
                    passwordEt.requestFocus()
                }
                password.length < 6 -> {
                    passwordEt.error = "Password must be at least 6 characters"
                    passwordEt.requestFocus()
                }
                password != confirmPassword -> {
                    confirmPasswordEt.error = "Passwords do not match"
                    confirmPasswordEt.requestFocus()
                }
                else -> {
                    // ✅ Passed all validations
                    Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

                    // Save user data in SharedPreferences
                    val sharedPref: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("name", "$firstName $lastName")
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()

                    // Redirect to Login (MainActivity)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        // 🔄 Already have an account → go to Sign In
        signInBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
