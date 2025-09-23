package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.R

class BusinessDetailsFragment : Fragment(R.layout.activity_business_details) {

    private lateinit var tvBusinessName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvBudget: TextView
    private lateinit var layoutRawMaterials: LinearLayout
    private lateinit var loaderLogo: ImageView
    private lateinit var scrollContainer: ScrollView

    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBusinessName = view.findViewById(R.id.tvBusinessName)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvBudget = view.findViewById(R.id.tvBudget)
        layoutRawMaterials = view.findViewById(R.id.layoutRawMaterials)
        loaderLogo = view.findViewById(R.id.loaderLogo)
        scrollContainer = view.findViewById(R.id.scrollContainer)

        // Initially hide content and show animated loader
        scrollContainer.visibility = View.GONE
        showLoader()

        val documentId = arguments?.getString("documentId")
        if (documentId != null) {
            fetchBusinessDetails(documentId)
        } else {
            hideLoader()
            scrollContainer.visibility = View.VISIBLE
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
                            setTextColor(resources.getColor(android.R.color.white))
                            textSize = 18f
                            setPadding(8, 8, 8, 8)
                        }
                        layoutRawMaterials.addView(textView)
                    }
                } else {
                    tvDescription.text = "No such document found."
                }

                hideLoader()
                scrollContainer.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                tvDescription.text = "Failed to fetch data: ${e.message}"
                hideLoader()
                scrollContainer.visibility = View.VISIBLE
            }
    }

    private fun showLoader() {
        loaderLogo.visibility = View.VISIBLE
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 600
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        loaderLogo.startAnimation(fadeIn)
    }

    private fun hideLoader() {
        loaderLogo.clearAnimation()
        loaderLogo.visibility = View.GONE
    }
}
