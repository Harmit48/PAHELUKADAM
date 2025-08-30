package com.pahekukadam.pahelukadam.ui.account

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pahekukadam.pahelukadam.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // ✅ FIX 1: Use the SAME SharedPreferences file name as the fragment.
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val nameEt: EditText = findViewById(R.id.editName)
        val emailEt: EditText = findViewById(R.id.editEmail)
        val saveBtn: Button = findViewById(R.id.btnSave)

        // Pre-fill existing data from "UserPrefs"
        val name = prefs.getString("name", "")
        val email = prefs.getString("email", "")
        nameEt.setText(name)
        emailEt.setText(email)

        saveBtn.setOnClickListener {
            val fullName = nameEt.text.toString().trim()
            val newEmail = emailEt.text.toString().trim()

            if (fullName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ FIX 2: Save the full name and email to match what the fragment reads.
            prefs.edit()
                .putString("name", fullName)
                .putString("email", newEmail)
                .apply()

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish() // Go back to the account screen
        }
    }
}