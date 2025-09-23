package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
    private lateinit var contentLayout: View
    private lateinit var logoLoader: View
    private lateinit var bestIdeaAdapter: BestIdeaAdapter

    private var currentIdeas: List<BestIdea> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvIdeas = view.findViewById(R.id.rvIdeas)
        contentLayout = view.findViewById(R.id.contentLayout)
        logoLoader = view.findViewById(R.id.logoLoader)

        // 🔥 Start loader animation
        contentLayout.visibility = View.GONE
        logoLoader.visibility = View.VISIBLE
        startLogoAnimation()

        setupRecyclerView()

        val selectedBudget = arguments?.getString("budget")
        val selectedCategory = arguments?.getString("category")

        if (selectedBudget != null && selectedCategory != null) {
            fetchFilteredIdeas(selectedBudget, selectedCategory)
        } else {
            Toast.makeText(requireContext(), "Error: Budget or Category not provided", Toast.LENGTH_LONG).show()
            showContent()
        }
    }

    private fun setupRecyclerView() {
        bestIdeaAdapter = BestIdeaAdapter(emptyList()) { clickedIdea ->
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
                currentIdeas = if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No ideas found for this selection.", Toast.LENGTH_LONG).show()
                    emptyList()
                } else {
                    documents.map { doc ->
                        BestIdea(
                            id = doc.id,
                            businessName = doc.getString("businessName") ?: "",
                            category_name = doc.getString("category_name") ?: "",
                            budget_range = doc.getString("budget_range") ?: ""
                        )
                    }
                }
                bestIdeaAdapter.updateIdeas(currentIdeas)
                showContent() // 🔥 Show content only after data is loaded
            }
            .addOnFailureListener { exception ->
                Log.w("BestIdeaFragment", "Error getting documents: ", exception)
                Toast.makeText(requireContext(), "Failed to load ideas. Please try again.", Toast.LENGTH_SHORT).show()
                showContent()
            }
    }

    private fun startLogoAnimation() {
        logoLoader.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                logoLoader.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        if (logoLoader.visibility == View.VISIBLE) startLogoAnimation()
                    }
                    .start()
            }
            .start()
    }

    private fun showContent() {
        logoLoader.clearAnimation()
        logoLoader.visibility = View.GONE
        contentLayout.alpha = 0f
        contentLayout.visibility = View.VISIBLE
        contentLayout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}
