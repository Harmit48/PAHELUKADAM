package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.ui.home.BestIdeaFragment

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

        // Dropdown setup
        acBudget.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_list_item_dropdown, budgets))
        acCategory.setAdapter(ArrayAdapter(requireContext(), R.layout.simple_list_item_dropdown, categories))

        acBudget.setOnClickListener { acBudget.showDropDown() }
        acCategory.setOnClickListener { acCategory.showDropDown() }

        btnShow.setOnClickListener {
            val selectedBudget = acBudget.text.toString()
            val selectedCategory = acCategory.text.toString()

            if (selectedBudget.isEmpty() || selectedCategory.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both Budget and Category", Toast.LENGTH_SHORT).show()
            } else {
                // ✅ Navigate to BestIdeaFragment using your existing container
                val fragment = BestIdeaFragment()

                val args = Bundle()
                args.putString("budget", selectedBudget)
                args.putString("category", selectedCategory)
                fragment.arguments = args

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment) // 👈 use the same container as HomeHubFragment
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
