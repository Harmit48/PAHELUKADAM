package com.pahelukadam.pahelukadam.ui.account

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import com.pahelukadam.pahelukadam.R

class ThemeDialog(context: Context) : Dialog(context) {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_theme)

        prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val lightRadio: RadioButton = findViewById(R.id.radioLight)
        val darkRadio: RadioButton = findViewById(R.id.radioDark)
        val saveBtn: Button = findViewById(R.id.btnSaveTheme)

        // Load saved theme
        when (prefs.getString("theme", "light")) {
            "light" -> lightRadio.isChecked = true
            "dark" -> darkRadio.isChecked = true
        }

        saveBtn.setOnClickListener {
            val selectedTheme = if (darkRadio.isChecked) "dark" else "light"

            prefs.edit().putString("theme", selectedTheme).apply()
            dismiss()
        }
    }
}
