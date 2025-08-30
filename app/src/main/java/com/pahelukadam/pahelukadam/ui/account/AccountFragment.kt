package com.example.pahelukadam.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pahelukadam.databinding.FragmentAccountBinding
import com.example.pahelukadam.ui.account.AddMobileActivity
import com.example.pahelukadam.ui.account.EditProfileActivity
import com.example.pahelukadam.MainActivity

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        binding.btnTheme.setOnClickListener {
            // TODO: Show theme dialog
        }

        binding.btnSignOut.setOnClickListener {
            // ✅ FIX: Use the correct SharedPreferences name here too.
            val sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0)
            sharedPref.edit().clear().apply()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // This correctly refreshes user data every time the fragment is visible
        loadUserData()
    }

    private fun loadUserData() {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0)
        val name = sharedPref.getString("name", "")
        val email = sharedPref.getString("email", "")

        binding.tvUserName.text = name
        binding.tvUserEmail.text = email
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}