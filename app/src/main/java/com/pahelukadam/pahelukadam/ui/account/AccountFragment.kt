package com.pahelukadam.pahelukadam.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
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
            clickResetHandler.removeCallbacksAndMessages(null)

            if (logoClickCount == 3) {
                logoClickCount = 0
                startActivity(Intent(requireContext(), Adminsigninpage::class.java))
            } else {
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

    /**
     * 🔄 Animate logo loader with smooth fade in/out loop
     */
    private fun startLogoAnimation() {
        val logo = binding.logoLoader
        logo.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                logo.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        if (logo.visibility == View.VISIBLE) startLogoAnimation()
                    }
                    .start()
            }
            .start()
    }

    /**
     * ✨ Smoothly show content after data load
     */
    private fun showContent() {
        binding.logoLoader.clearAnimation()
        binding.logoLoader.visibility = View.GONE
        binding.contentLayout.alpha = 0f
        binding.contentLayout.visibility = View.VISIBLE
        binding.contentLayout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun loadUserData() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // 🔄 Show loader animation, hide content
            binding.logoLoader.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
            startLogoAnimation()

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
                    showContent() // ✅ smooth transition after loading
                }
                .addOnFailureListener { exception ->
                    Log.e("AccountFragment", "Error fetching user data", exception)
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                    showContent() // show content even on failure
                }
        } else {
            // If no user is logged in
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
