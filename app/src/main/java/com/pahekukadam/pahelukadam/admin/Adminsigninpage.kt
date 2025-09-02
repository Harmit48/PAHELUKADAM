package com.pahekukadam.pahelukadam.admin

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahekukadam.pahelukadam.R

class Adminsigninpage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminsigninpage)

        auth = Firebase.auth

        // ✅ Apply Gradient to App Name TextView
        val appName: TextView = findViewById(R.id.appName)
        val paint = appName.paint
        val width = paint.measureText(appName.text.toString())
        val textShader = LinearGradient(
            0f, 0f, width, appName.textSize,
            intArrayOf(
                android.graphics.Color.parseColor("#F48C06"), // Orange
                android.graphics.Color.parseColor("#DC0202")  // Red
            ),
            null,
            Shader.TileMode.CLAMP
        )
        appName.paint.shader = textShader

        val signInBtn: Button = findViewById(R.id.adminSignInBtn)
        val emailField: TextInputEditText = findViewById(R.id.emailField)
        val passwordField: TextInputEditText = findViewById(R.id.passwordField)

        signInBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signInAsAdmin(email, password)
        }
    }

    private fun signInAsAdmin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val db = Firebase.firestore
                        db.collection("admins")
                            .whereEqualTo("email", user.email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    Toast.makeText(this, "Admin Sign-In Successful!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, AdminActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Access Denied: Not an admin account.", Toast.LENGTH_LONG).show()
                                    auth.signOut()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to check admin status.", Toast.LENGTH_LONG).show()
                                auth.signOut()
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
