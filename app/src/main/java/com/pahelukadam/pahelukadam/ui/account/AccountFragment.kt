package com.pahelukadam.pahelukadam.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    // Counter for logo clicks
    private var logoClickCount = 0
    private val clickResetHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        // ✅ Secret Feature: Triple Click Logo -> Adminsigninpage
        binding.logo.setOnClickListener {
            logoClickCount++
            clickResetHandler.removeCallbacksAndMessages(null) // reset timer

            if (logoClickCount == 3) {
                logoClickCount = 0 // reset count
                startActivity(Intent(requireContext(), Adminsigninpage::class.java))
            } else {
                // reset counter if no click within 1.5 seconds
                clickResetHandler.postDelayed({ logoClickCount = 0 }, 1500)
            }
        }

        // Edit Profile button
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Add Mobile button
        binding.btnAddMobile.setOnClickListener {
            startActivity(Intent(requireContext(), AddMobileActivity::class.java))
        }

        // Sign Out button
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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
            // 🔄 Show loader, hide content
            binding.logoLoader.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE

            val db = Firebase.firestore
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    // ✅ Hide loader, show content
                    binding.logoLoader.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE

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
                    // ❌ Hide loader, show content even on failure
                    binding.logoLoader.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                    Log.e("AccountFragment", "Error fetching user data", exception)
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
        } else {
            // If no user is logged in, hide loader and show content with default values
            binding.logoLoader.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
            binding.tvUserName.text = "Guest User"
            binding.tvUserEmail.text = "Not signed in"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
