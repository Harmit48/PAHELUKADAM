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

        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }

        val materials = mutableListOf<Map<String, Any>>()
        for (i in 0 until containerRawMaterials.childCount) {
            val child = containerRawMaterials.getChildAt(i)
            val title = child.findViewById<EditText>(R.id.etRawTitle).text.toString().trim()
            val price = child.findViewById<EditText>(R.id.etRawPrice).text.toString().toDoubleOrNull()

            if (title.isNotEmpty() && price != null) {
                materials.add(mapOf("title" to title, "price" to price))
            }
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
