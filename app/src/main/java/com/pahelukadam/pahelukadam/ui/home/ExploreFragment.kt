package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.adapter.BusinessAdapter
import com.pahelukadam.pahelukadam.databinding.FragmentExploreBinding
import com.pahelukadam.pahelukadam.model.BusinessIdea

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    // Initialize Firestore and the list for business ideas
    private val db = FirebaseFirestore.getInstance()
    private val ideaList = mutableListOf<BusinessIdea>()
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        setupRecyclerView()

        // Fetch data from Firestore
        fetchIdeas()

        return binding.root
    }

    private fun setupRecyclerView() {
        // Initialize the adapter with an empty list
        businessAdapter = BusinessAdapter(ideaList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = businessAdapter
        }
    }

    private fun fetchIdeas() {
        // Show loading indicator
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        db.collection("explore")
            .get()
            .addOnSuccessListener { result ->
                // Hide loading indicator
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                ideaList.clear() // Clear the list to avoid duplicates
                for (document in result) {
                    // Convert each document into a BusinessIdea object
                    val idea = document.toObject(BusinessIdea::class.java)
                    ideaList.add(idea)
                }
                // Notify the adapter that the data has changed
                businessAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Hide loading indicator on failure
                binding.progressBar.visibility = View.GONE

                // Log the error and show a message to the user
                Log.e("ExploreFragment", "Error getting documents: ", exception)
                Toast.makeText(context, "Error fetching ideas.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}