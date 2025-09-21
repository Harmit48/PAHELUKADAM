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
    private lateinit var progressBar: ProgressBar
    private lateinit var bestIdeaAdapter: BestIdeaAdapter

    private var currentIdeas: List<BestIdea> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvIdeas = view.findViewById(R.id.rvIdeas)
        setupRecyclerView()

        val selectedBudget = arguments?.getString("budget")
        val selectedCategory = arguments?.getString("category")

        if (selectedBudget != null && selectedCategory != null) {
            fetchFilteredIdeas(selectedBudget, selectedCategory)
        } else {
            Toast.makeText(requireContext(), "Error: Budget or Category not provided", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        bestIdeaAdapter = BestIdeaAdapter(emptyList()) { clickedIdea ->
            // ✅ Navigate to BusinessDetailsFragment with docId
            val fragment = BusinessDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("documentId", clickedIdea.id)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        rvIdeas.layoutManager = LinearLayoutManager(requireContext())
        rvIdeas.adapter = bestIdeaAdapter
    }

    private fun fetchFilteredIdeas(budget: String, category: String) {
        db.collection("business_ideas")
            .whereEqualTo("budget_range", budget)
            .whereEqualTo("category_name", category)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No ideas found for this selection.", Toast.LENGTH_LONG).show()
                    currentIdeas = emptyList()
                    bestIdeaAdapter.updateIdeas(currentIdeas)
                } else {
                    val ideasList = documents.map { doc ->
                        BestIdea(
                            id = doc.id,
                            businessName = doc.getString("businessName") ?: "",
                            category_name = doc.getString("category_name") ?: "",
                            budget_range = doc.getString("budget_range") ?: ""
                        )
                    }
                    currentIdeas = ideasList
                    bestIdeaAdapter.updateIdeas(ideasList)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("BestIdeaFragment", "Error getting documents: ", exception)
                Toast.makeText(requireContext(), "Failed to load ideas. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }
}
