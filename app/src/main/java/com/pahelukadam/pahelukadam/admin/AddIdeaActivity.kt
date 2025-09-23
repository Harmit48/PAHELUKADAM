package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.R

class AddIdeaActivity : AppCompatActivity() {

    private lateinit var containerRawMaterials: LinearLayout
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_idea)

        containerRawMaterials = findViewById(R.id.containerRawMaterials)
        val btnAddRawMaterial: ImageButton = findViewById(R.id.btnAddRawMaterial)
        val btnAddIdea = findViewById<android.widget.Button>(R.id.btnAddIdea)

        addRawMaterialView()

        btnAddRawMaterial.setOnClickListener { addRawMaterialView() }
        btnAddIdea.setOnClickListener { submitIdea() }
    }

    private fun addRawMaterialView() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_raw_material, containerRawMaterials, false)

        val btnRemove = view.findViewById<ImageButton>(R.id.btnRemoveRawMaterial)
        btnRemove.setOnClickListener { containerRawMaterials.removeView(view) }

        containerRawMaterials.addView(view)
    }

    private fun submitIdea() {
        val etName = findViewById<EditText>(R.id.etName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etBudgetMin = findViewById<EditText>(R.id.etBudgetMin)
        val etBudgetMax = findViewById<EditText>(R.id.etBudgetMax)

        val name = etName.text.toString().trim()
        val desc = etDescription.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val minBudget = etBudgetMin.text.toString().trim()
        val maxBudget = etBudgetMax.text.toString().trim()

        // ✅ Validate main fields
        when {
            name.isEmpty() -> {
                etName.error = "Name is required"
                return
            }
            desc.isEmpty() -> {
                etDescription.error = "Description is required"
                return
            }
            category.isEmpty() -> {
                etCategory.error = "Category is required"
                return
            }
            minBudget.isEmpty() -> {
                etBudgetMin.error = "Minimum budget is required"
                return
            }
            maxBudget.isEmpty() -> {
                etBudgetMax.error = "Maximum budget is required"
                return
            }
        }

        // ✅ Validate raw material inputs
        val materials = mutableListOf<Map<String, Any>>()
        for (i in 0 until containerRawMaterials.childCount) {
            val child = containerRawMaterials.getChildAt(i)
            val etTitle = child.findViewById<EditText>(R.id.etRawTitle)
            val etPrice = child.findViewById<EditText>(R.id.etRawPrice)

            val title = etTitle.text.toString().trim()
            val priceText = etPrice.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Material name required"
                return
            }
            if (priceText.isEmpty()) {
                etPrice.error = "Price required"
                return
            }

            val price = priceText.toDoubleOrNull()
            if (price == null || price <= 0) {
                etPrice.error = "Enter valid price"
                return
            }

            materials.add(mapOf("title" to title, "price" to price))
        }

        val ideaData = hashMapOf(
            "businessName" to name,
            "description" to desc,
            "category_name" to category,
            "budget_range" to "$minBudget - $maxBudget",
            "rawMaterials" to materials
        )

        firestore.collection("business_ideas")
            .add(ideaData)
            .addOnSuccessListener {
                Toast.makeText(this, "Idea saved successfully", Toast.LENGTH_SHORT).show()
                finish() // ✅ closes and Home auto-refreshes
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
