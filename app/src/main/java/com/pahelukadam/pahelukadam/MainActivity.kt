package com.pahelukadam.pahelukadam

import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.ui.HubActivity
import com.pahelukadam.pahelukadam.utils.FirestoreSeeder

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Theme preference
        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val theme = prefs.getString("theme", "light")
        when (theme) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        // ✅ First-time Firestore seeding
        FirestoreSeeder.seedDatabase(this)

        // ✅ Auto login check
        if (auth.currentUser != null) {
            val intent = Intent(this, HubActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // ✅ Gradient text for app name
        val appName: TextView = findViewById(R.id.appName)
        val paint = appName.paint
        val width = paint.measureText(appName.text.toString())
        val textShader = LinearGradient(
            0f, 0f, width, appName.textSize,
            intArrayOf(
                android.graphics.Color.parseColor("#F48C06"),
                android.graphics.Color.parseColor("#DC0202")
            ),
            null,
            Shader.TileMode.CLAMP
        )
        appName.paint.shader = textShader

        // ✅ UI References
        val signInBtn: Button = findViewById(R.id.signInBtn)
        val signUpBtn: Button = findViewById(R.id.signUpBtn)
        val emailField: TextInputEditText = findViewById(R.id.emailField)
        val passwordField: TextInputEditText = findViewById(R.id.passwordField)

        // ✅ Sign in
        signInBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // 🔹 Validation
            when {
                email.isEmpty() -> {
                    emailField.error = "Email is required"
                    return@setOnClickListener
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailField.error = "Enter a valid email"
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    passwordField.error = "Password is required"
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    passwordField.error = "Password must be at least 6 characters"
                    return@setOnClickListener
                }
            }

            // 🔹 Firebase authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // 🔍 Check Firestore user document
                        db.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // ✅ User exists → Show toast & go to HubActivity
                                    Toast.makeText(
                                        this,
                                        "Login Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this, HubActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // ❌ User not found
                                    Toast.makeText(
                                        this,
                                        "User not found in database",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    auth.signOut()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Error checking user data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        passwordField.error = "Authentication failed"
                    }
                }
        }

        // ✅ Sign up
        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
