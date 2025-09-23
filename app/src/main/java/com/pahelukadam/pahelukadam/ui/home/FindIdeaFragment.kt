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

    private lateinit var acBudget: AutoCompleteTextView
    private lateinit var acCategory: AutoCompleteTextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acBudget = view.findViewById(R.id.acBudget)
        acCategory = view.findViewById(R.id.acCategory)
        val btnShow = view.findViewById<MaterialButton>(R.id.btnShowIdea)

        val budgets = listOf(
            "₹50,000 - ₹1,00,000",
            "₹1,00,000 - ₹2,00,000",
            "₹2,00,000 - ₹3,00,000",
            "₹3,00,000 - ₹5,00,000",
            "₹5,00,000 - ₹7,00,000",
            "₹7,00,000 - ₹10,00,000"
        )

        // Make sure these category names exactly match the ones in your Firestore database
        val categories = listOf(
            "Healthcare & Wellness",
            "Manufacturing",
            "Retail",
            "Food & Beverage",
            "Education",
            "Manufacturing (Food)",
            "Logistics & Storage",
            "Digital Marketing Agency",
            "Agriculture & Farming",
            "Services"
            // Add any other categories you have
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
                val formattedBudget = selectedBudget
                    .replace("₹", "")
                    .replace(",", "")

                val fragment = BestIdeaFragment()
                val args = Bundle()
                args.putString("budget", formattedBudget)
                args.putString("category", selectedCategory)
                fragment.arguments = args

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // ✅ Clear selections every time the user comes back to this fragment
        acBudget.setText("")
        acCategory.setText("")
    }
}
