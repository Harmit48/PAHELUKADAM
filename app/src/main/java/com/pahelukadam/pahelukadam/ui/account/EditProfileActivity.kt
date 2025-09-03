package com.pahelukadam.pahelukadam.ui.account

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView // <-- Import TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.R

class EditProfileActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser

    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var emailTv: TextView
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firstNameEt = findViewById(R.id.editFirstName)
        lastNameEt = findViewById(R.id.editLastName)
        emailTv = findViewById(R.id.editEmail)
        saveBtn = findViewById(R.id.btnSave)

        loadUserData()

        saveBtn.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun loadUserData() {
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        firstNameEt.setText(document.getString("firstName"))
                        lastNameEt.setText(document.getString("lastName"))
                        emailTv.text = document.getString("email")
                    }
                }
        }
    }

    private fun saveProfileChanges() {
        val newFirstName = firstNameEt.text.toString().trim()
        val newLastName = lastNameEt.text.toString().trim()

        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            Toast.makeText(this, "Name fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUser == null) {
            Toast.makeText(this, "Error: Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userUpdates = mapOf(
            "firstName" to newFirstName,
            "lastName" to newLastName
        )

        db.collection("users").document(currentUser.uid).update(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}