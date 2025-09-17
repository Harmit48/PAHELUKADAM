package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.adapter.BestIdeaAdapter
import com.pahelukadam.pahelukadam.model.BestIdea

class BestIdeaFragment : Fragment(R.layout.fragment_best_idea) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvIdeas = view.findViewById<RecyclerView>(R.id.rvIdeas)

        val ideas = listOf(
            BestIdea("AI - Powered chatbot solutions", "Tech", "₹5,00,000 - ₹7,00,000"),
            BestIdea("Dairy Products (Paneer, Ghee)", "Food and Beverage", "₹3,00,000 - ₹5,00,000"),
            BestIdea("AI/ML Based Data Analytics Services", "Tech", "₹7,00,000 - ₹10,00,000"),
            BestIdea("Cafe Shop", "Food and Beverage", "₹7,00,000 - ₹10,00,000"),
            BestIdea("T-Shirt Printing & Making Unit", "Textile", "₹2,00,000 - ₹3,00,000"),
            BestIdea("Casual Dining Restaurant (Regional Food)", "Food and Beverage", "₹10,00,000+")
        )

        rvIdeas.layoutManager = LinearLayoutManager(requireContext())
        rvIdeas.adapter = BestIdeaAdapter(ideas)
    }
}
