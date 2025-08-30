package com.example.pahelukadam.ui.account

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pahelukadam.R

class AddMobileActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mobile)

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val mobileEt: EditText = findViewById(R.id.editMobile)
        val saveBtn: Button = findViewById(R.id.btnSaveMobile)

        // Pre-fill if exists
        mobileEt.setText(prefs.getString("mobile", ""))

        saveBtn.setOnClickListener {
            val mobile = mobileEt.text.toString().trim()

            if (mobile.length < 10) {
                Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString("mobile", mobile)
                .apply()

            Toast.makeText(this, "Mobile Number Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
