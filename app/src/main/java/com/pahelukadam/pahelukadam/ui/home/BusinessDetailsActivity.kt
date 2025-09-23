package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.R

class BusinessDetailsActivity : AppCompatActivity() {

    private lateinit var tvBusinessName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvBudget: TextView
    private lateinit var layoutRawMaterials: LinearLayout
    private lateinit var loaderLogo: ImageView
    private lateinit var scrollContainer: ScrollView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        tvBusinessName = findViewById(R.id.tvBusinessName)
        tvDescription = findViewById(R.id.tvDescription)
        tvBudget = findViewById(R.id.tvBudget)
        layoutRawMaterials = findViewById(R.id.layoutRawMaterials)
        loaderLogo = findViewById(R.id.loaderLogo)
        scrollContainer = findViewById(R.id.scrollContainer)

        // Hide content and show animated logo loader
        scrollContainer.visibility = View.GONE
        showLoader()

        // Fetch data from Firestore (example: fixed document ID)
        fetchBusinessDetails("OlHfU5XuqLAdIHi1eM0q")
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

                    // Populate raw materials dynamically
                    layoutRawMaterials.removeAllViews()
                    rawMaterials?.forEachIndexed { index, item ->
                        val itemTitle = item["title"]?.toString() ?: "Unknown"
                        val itemPrice = item["price"]?.toString() ?: "N/A"

                        val textView = TextView(this).apply {
                            text = "${index + 1}. $itemTitle\nPrice: ₹$itemPrice"
                            setTextColor(ContextCompat.getColor(context, android.R.color.white))
                            textSize = 20f
                            setPadding(8, 8, 8, 8)
                        }
                        layoutRawMaterials.addView(textView)
                    }
                } else {
                    tvDescription.text = "No such document found."
                }

                // Hide loader and show content
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
        val fadeInOut = AlphaAnimation(0f, 1f).apply {
            duration = 700
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        loaderLogo.startAnimation(fadeInOut)
    }

    private fun hideLoader() {
        loaderLogo.clearAnimation()
        loaderLogo.visibility = View.GONE
    }
}
