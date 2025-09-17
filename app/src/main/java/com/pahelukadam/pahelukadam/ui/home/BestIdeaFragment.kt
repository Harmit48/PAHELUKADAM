package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.adapter.BestIdeaAdapter
import com.pahelukadam.pahelukadam.model.BestIdea

class BestIdeaFragment : Fragment(R.layout.fragment_best_idea) {

    private val db = Firebase.firestore
    private lateinit var rvIdeas: RecyclerView
    private lateinit var progressBar: ProgressBar // Optional: For better UX
    private lateinit var bestIdeaAdapter: BestIdeaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvIdeas = view.findViewById(R.id.rvIdeas)
        // Make sure you have a ProgressBar with this ID in your fragment_best_idea.xml
        // progressBar = view.findViewById(R.id.progressBar)

        setupRecyclerView()

        // Retrieve arguments
        val selectedBudget = arguments?.getString("budget")
        val selectedCategory = arguments?.getString("category")

        if (selectedBudget != null && selectedCategory != null) {
            fetchFilteredIdeas(selectedBudget, selectedCategory)
        } else {
            Toast.makeText(requireContext(), "Error: Budget or Category not provided", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        // Initialize adapter with an empty list first
        bestIdeaAdapter = BestIdeaAdapter(emptyList())
        rvIdeas.layoutManager = LinearLayoutManager(requireContext())
        rvIdeas.adapter = bestIdeaAdapter
    }

    private fun fetchFilteredIdeas(budget: String, category: String) {
        // progressBar.visibility = View.VISIBLE // Show loading indicator

        db.collection("business_ideas")
            .whereEqualTo("budget_range", budget)
            .whereEqualTo("category_name", category)
            .get()
            .addOnSuccessListener { documents ->
                // progressBar.visibility = View.GONE // Hide loading indicator
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No ideas found for this selection.", Toast.LENGTH_LONG).show()
                } else {
                    // Convert the query result to a list of BestIdea objects
                    val ideasList = documents.toObjects(BestIdea::class.java)
                    bestIdeaAdapter.updateIdeas(ideasList) // Update adapter data
                }
            }
            .addOnFailureListener { exception ->
                // progressBar.visibility = View.GONE // Hide loading indicator
                Log.w("BestIdeaFragment", "Error getting documents: ", exception)
                Toast.makeText(requireContext(), "Failed to load ideas. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }
}