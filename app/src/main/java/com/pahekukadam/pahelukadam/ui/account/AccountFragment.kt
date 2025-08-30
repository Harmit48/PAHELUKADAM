package com.pahekukadam.pahelukadam.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pahekukadam.pahelukadam.MainActivity
import com.pahekukadam.pahelukadam.databinding.FragmentAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    // View binding to safely access UI elements
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        // Set up the click listener for the sign-out button
        binding.btnSignOut.setOnClickListener {
            // Sign the user out of their Firebase account
            Firebase.auth.signOut()

            // Create an intent to go back to the MainActivity (login screen)
            val intent = Intent(requireContext(), MainActivity::class.java)
            // Clear the activity stack so the user can't press "back" to get into the app
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Return the root view of the fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Load user data every time the fragment becomes visible
        // This ensures the data is fresh if the user edits their profile elsewhere
        loadUserData()
    }

    // Function to fetch and display user data from Firestore
    private fun loadUserData() {
        // Get the current signed-in user from Firebase Authentication
        val currentUser = Firebase.auth.currentUser

        // Proceed only if a user is actually logged in
        if (currentUser != null) {
            val uid = currentUser.uid
            val db = Firebase.firestore

            // Fetch the document corresponding to the user's UID from the "users" collection
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    // Check if the document was found
                    if (document != null && document.exists()) {
                        // Retrieve the first name, last name, and email from the document
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val email = document.getString("email") ?: ""

                        // Combine the names and set the text on the UI elements
                        binding.tvUserName.text = "$firstName $lastName"
                        binding.tvUserEmail.text = email
                    } else {
                        // Log a message if the user's document wasn't found in Firestore
                        Log.d("AccountFragment", "No user profile document found")
                    }
                }
                .addOnFailureListener { exception ->
                    // Log an error and show a message if fetching the data fails
                    Log.e("AccountFragment", "Error fetching user data", exception)
                    Toast.makeText(requireContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding reference when the view is destroyed to prevent memory leaks
        _binding = null
    }
}