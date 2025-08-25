package com.example.pahelukadam.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pahelukadam.MainActivity
import com.example.pahelukadam.R

class AccountFragment : Fragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)

        val btnEditProfile: View = view.findViewById(R.id.btnEditProfile)
        val btnAddMobile: View = view.findViewById(R.id.btnAddMobile)
        val btnSignOut: View = view.findViewById(R.id.btnSignOut)

        // Load user data
        val sharedPrefs = requireActivity()
            .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val name = sharedPrefs.getString("name", "Guest User")
        val email = sharedPrefs.getString("email", "guest@example.com")

        // Show name in both places
        tvName.text = name               // Main display name
        tvUserName.text = name           // Replace @guest with full name
        tvEmail.text = email

        // Actions
        btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        btnSignOut.setOnClickListener {
            sharedPrefs.edit().clear().apply()

            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}
