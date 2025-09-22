package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.widget.LinearLayout
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

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        tvBusinessName = findViewById(R.id.tvBusinessName)
        tvDescription = findViewById(R.id.tvDescription)
        tvBudget = findViewById(R.id.tvBudget)
        layoutRawMaterials = findViewById(R.id.layoutRawMaterials)

        // Fetch data from Firestore using fixed document ID
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

                    // Clear and populate raw materials dynamically
                    layoutRawMaterials.removeAllViews()
                    rawMaterials?.forEachIndexed { index, item ->
                        val itemTitle = item["title"]?.toString() ?: "Unknown"
                        val itemPrice = item["price"]?.toString() ?: "N/A"

                        val textView = TextView(this).apply {
                            text = "${index + 1}. $itemTitle\nPrice: ₹$itemPrice"
                            setTextColor(ContextCompat.getColor(context, android.R.color.white)) // ✅ White text
                            textSize = 20f
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
