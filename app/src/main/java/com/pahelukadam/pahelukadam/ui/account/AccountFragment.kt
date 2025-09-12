package com.pahelukadam.pahelukadam.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.MainActivity
import com.pahelukadam.pahelukadam.admin.Adminsigninpage
import com.pahelukadam.pahelukadam.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        // Edit Profile button
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Add Mobile button
        binding.btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        // Save button
        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Account details saved!", Toast.LENGTH_SHORT).show()
            // TODO: Add your save logic here (e.g., update Firestore or SharedPreferences)
        }

        // Sign Out button
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Admin button
        binding.AdminBtn.setOnClickListener {
            startActivity(Intent(requireContext(), Adminsigninpage::class.java))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val db = Firebase.firestore
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val email = document.getString("email") ?: ""

                        binding.tvUserName.text = "$firstName $lastName"
                        binding.tvUserEmail.text = email
                    } else {
                        Log.d("AccountFragment", "No user profile document found")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AccountFragment", "Error fetching user data", exception)
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
