package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.pahelukadam.pahelukadam.R

class FindIdeaFragment : Fragment(R.layout.fragment_findidea) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val acBudget = view.findViewById<AutoCompleteTextView>(R.id.acBudget)
        val acCategory = view.findViewById<AutoCompleteTextView>(R.id.acCategory)
        val btnShow = view.findViewById<MaterialButton>(R.id.btnShowIdea)

        val budgets = listOf(
            "₹50,000 - ₹1,00,000",
            "₹1,00,000 - ₹2,00,000",
            "₹2,00,000 - ₹3,00,000",
            "₹3,00,000 - ₹5,00,000",
            "₹5,00,000 - ₹7,00,000",
            "₹7,00,000 - ₹10,00,000"
        )

        val categories = listOf(
            "Services",
            "Manufacturing",
            "Retail",
            "Food & Beverage",
            "Education",
            "Manufacturing (Food)",
            "Logistics & Storage"
        )

        acBudget.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_list_item_dropdown, budgets))
        acCategory.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_list_item_dropdown, categories))

        acBudget.setOnClickListener { acBudget.showDropDown() }
        acCategory.setOnClickListener { acCategory.showDropDown() }

        btnShow.setOnClickListener {
            val b = acBudget.text.toString()
            val c = acCategory.text.toString()
            if (b.isBlank() || c.isBlank()) {
                Toast.makeText(requireContext(), "Please select Budget and Category", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Budget: $b\nCategory: $c", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
