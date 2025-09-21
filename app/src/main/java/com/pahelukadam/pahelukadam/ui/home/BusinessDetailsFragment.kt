package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.R

class BusinessDetailsFragment : Fragment(R.layout.activity_business_details) {

    private lateinit var tvBusinessName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvBudget: TextView
    private lateinit var layoutRawMaterials: LinearLayout

    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBusinessName = view.findViewById(R.id.tvBusinessName)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvBudget = view.findViewById(R.id.tvBudget)
        layoutRawMaterials = view.findViewById(R.id.layoutRawMaterials)

        val documentId = arguments?.getString("documentId")
        if (documentId != null) {
            fetchBusinessDetails(documentId)
        } else {
            tvDescription.text = "No document ID provided."
        }
    }

    private fun fetchBusinessDetails(documentId: String) {
        db.collection("business_ideas")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("businessName") ?: ""
                    val description = document.getString("description") ?: ""
                    val budget = document.getString("budget_range") ?: ""
                    val rawMaterials = document.get("rawMaterials") as? List<Map<String, Any>>

                    tvBusinessName.text = name
                    tvDescription.text = description
                    tvBudget.text = "Budget: $budget"

                    layoutRawMaterials.removeAllViews()
                    rawMaterials?.forEachIndexed { index, item ->
                        val itemTitle = item["title"]?.toString() ?: "Unknown"
                        val itemPrice = item["price"]?.toString() ?: "N/A"

                        val textView = TextView(requireContext()).apply {
                            text = "${index + 1}. $itemTitle\nPrice: ₹$itemPrice"
                            setTextColor(resources.getColor(android.R.color.black))
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }
                        layoutRawMaterials.addView(textView)
                    }
                } else {
                    tvDescription.text = "No such document found."
                }
            }
            .addOnFailureListener { e ->
                tvDescription.text = "Failed to fetch data: ${e.message}"
            }
    }
}
