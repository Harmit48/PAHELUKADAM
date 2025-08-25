package com.example.pahelukadam.ui.account

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pahelukadam.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val nameEt: EditText = findViewById(R.id.editName)
        val emailEt: EditText = findViewById(R.id.editEmail)
        val saveBtn: Button = findViewById(R.id.btnSave)

        // Pre-fill existing data
        nameEt.setText(prefs.getString("name", ""))
        emailEt.setText(prefs.getString("email", ""))

        saveBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString("name", name)
                .putString("email", email)
                .apply()

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
