package com.pahekukadam.pahelukadam

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        // 🌈 Gradient for App Name (Your existing code is perfect)
        val appName: TextView = findViewById(R.id.appName)
        val paint = appName.paint
        val width = paint.measureText(appName.text.toString())
        val textShader = LinearGradient(
            0f, 0f, width, appName.textSize,
            intArrayOf(
                android.graphics.Color.parseColor("#F48C06"),
                android.graphics.Color.parseColor("#DC0202") // Corrected hex color
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

            // Your existing validation logic is great and remains unchanged
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
                    // --- START: FIREBASE INTEGRATION ---

                    // 4. Create the user in Firebase Authentication
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Authentication was successful, now save user data to Firestore
                                Log.d("SignUpActivity", "createUserWithEmail:success")
                                val firebaseUser = auth.currentUser
                                val uid = firebaseUser!!.uid // Get the unique ID for the new user

                                // Get Firestore instance
                                val db = Firebase.firestore

                                // Create a data map for the user's profile
                                val userProfile = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "email" to email
                                )

                                // 5. Save the profile to a "users" collection in Firestore
                                db.collection("users").document(uid).set(userProfile)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

                                        // Redirect to Login (MainActivity)
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("SignUpActivity", "Error adding document", e)
                                        Toast.makeText(this, "Error saving user details.", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    // --- END: FIREBASE INTEGRATION ---
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