package com.pahelukadam.pahelukadam.ui.account

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.R

class AddMobileActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser

    private lateinit var mobileEt: EditText
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mobile)

        mobileEt = findViewById(R.id.editMobile)
        saveBtn = findViewById(R.id.btnSaveMobile)

        // Load the existing mobile number from Firestore to pre-fill the field
        loadMobileNumber()

        saveBtn.setOnClickListener {
            saveMobileNumber()
        }
    }
    private fun loadMobileNumber() {
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        mobileEt.setText(document.getString("mobile"))
                    }
                }
        }
    }
    private fun saveMobileNumber() {
        val mobile = mobileEt.text.toString().trim()

        if (mobile.length < 10) {
            Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUser != null) {
            val userUpdate = mapOf(
                "mobile" to mobile
            )

            db.collection("users").document(currentUser.uid).update(userUpdate)
                .addOnSuccessListener {
                    Toast.makeText(this, "Mobile Number Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}