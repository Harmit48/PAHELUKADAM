package com.pahelukadam.pahelukadam.ui.home

import android.content.Intent
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

    // Keep a local copy of the currently displayed ideas so clicks can access them
    private var currentIdeas: List<BestIdea> = emptyList()

    // Listener that attaches click handlers to item views (no adapter change required)
    private val childAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = rvIdeas.getChildViewHolder(view)
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION && pos < currentIdeas.size) {
                    val clickedIdea = currentIdeas[pos]
                    val intent = Intent(requireContext(), BusinessDetailsActivity::class.java).apply {
                        putExtra("businessName", clickedIdea.businessName)
                        putExtra("category_name", clickedIdea.category_name)
                        putExtra("budget_range", clickedIdea.budget_range)
                    }
                    startActivity(intent)
                }
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            // Clear listener to avoid accidental reuse
            view.setOnClickListener(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvIdeas = view.findViewById(R.id.rvIdeas)
        // If you do have a ProgressBar in layout, uncomment and set its id
        // progressBar = view.findViewById(R.id.progressBar)

        setupRecyclerView()

        // Add the attach-state listener so clicking works without changing adapter
        rvIdeas.addOnChildAttachStateChangeListener(childAttachListener)

        val selectedBudget = arguments?.getString("budget")
        val selectedCategory = arguments?.getString("category")

        if (selectedBudget != null && selectedCategory != null) {
            fetchFilteredIdeas(selectedBudget, selectedCategory)
        } else {
            Toast.makeText(requireContext(), "Error: Budget or Category not provided", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        // Use the adapter's existing constructor (single argument)
        bestIdeaAdapter = BestIdeaAdapter(emptyList())
        rvIdeas.layoutManager = LinearLayoutManager(requireContext())
        rvIdeas.adapter = bestIdeaAdapter
    }

    private fun fetchFilteredIdeas(budget: String, category: String) {
        // progressBar.visibility = View.VISIBLE // optional
        db.collection("business_ideas")
            .whereEqualTo("budget_range", budget)
            .whereEqualTo("category_name", category)
            .get()
            .addOnSuccessListener { documents ->
                // progressBar.visibility = View.GONE
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "No ideas found for this selection.", Toast.LENGTH_LONG).show()
                    currentIdeas = emptyList()
                    bestIdeaAdapter.updateIdeas(currentIdeas)
                } else {
                    val ideasList = documents.toObjects(BestIdea::class.java)
                    currentIdeas = ideasList
                    bestIdeaAdapter.updateIdeas(ideasList)
                }
            }
            .addOnFailureListener { exception ->
                // progressBar.visibility = View.GONE
                Log.w("BestIdeaFragment", "Error getting documents: ", exception)
                Toast.makeText(requireContext(), "Failed to load ideas. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove listener to prevent leaks
        if (::rvIdeas.isInitialized) {
            try {
                rvIdeas.removeOnChildAttachStateChangeListener(childAttachListener)
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}
