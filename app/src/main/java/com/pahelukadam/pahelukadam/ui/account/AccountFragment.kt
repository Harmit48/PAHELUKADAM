package com.pahelukadam.pahelukadam.ui.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pahelukadam.pahelukadam.R

class AccountFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        profileImage = view.findViewById(R.id.profileImage)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPassword = view.findViewById(R.id.tvPassword)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Fetch from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "Guest User")
        val email = sharedPref.getString("email", "guest@email.com")
        val password = sharedPref.getString("password", "********")

        tvName.text = "Name: $name"
        tvEmail.text = "Email: $email"
        tvPassword.text = "Password: $password"

        btnLogout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            requireActivity().finish()
        }

        return view
    }
}
