package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.pahelukadam.pahelukadam.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddideaActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etCategory: EditText
    private lateinit var etMinBudget: EditText
    private lateinit var etMaxBudget: EditText
    private lateinit var btnAddIdea: Button
    private lateinit var btnAddRaw: ImageButton
    private lateinit var rawMaterialsContainer: LinearLayout

    private val firestore = FirebaseFirestore.getInstance()
    private val rawMaterialViews = mutableListOf<LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_addidea)

        // Bind views
        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        etCategory = findViewById(R.id.etCategory)
        etMinBudget = findViewById(R.id.etMinBudget)
        etMaxBudget = findViewById(R.id.etMaxBudget)
        btnAddIdea = findViewById(R.id.btnAddIdea)
        btnAddRaw = findViewById(R.id.btnAddRaw)
        rawMaterialsContainer = findViewById(R.id.rawMaterialsContainer)

        // Add the first raw material box by default
        addRawMaterialBox()

        // Add a new raw material box when clicking the "+" button
        btnAddRaw.setOnClickListener {
            addRawMaterialBox()
        }

        // Save idea and raw materials
        btnAddIdea.setOnClickListener {
            saveIdeaToFirestore()
        }
    }

    /** Function to dynamically add a new raw material box */
    private fun addRawMaterialBox() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_raw_material, rawMaterialsContainer, false) as LinearLayout
        rawMaterialsContainer.addView(view)
        rawMaterialViews.add(view)
    }

    /** Function to save idea data into Firestore */
    private fun saveIdeaToFirestore() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val minBudget = etMinBudget.text.toString().toDoubleOrNull() ?: 0.0
        val maxBudget = etMaxBudget.text.toString().toDoubleOrNull() ?: 0.0

        // Validate required fields
        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Collect raw materials from dynamically added boxes
        val rawMaterials = mutableListOf<Map<String, Any>>()
        for (view in rawMaterialViews) {
            val title = view.findViewById<EditText>(R.id.etRawTitle).text.toString().trim()
            val priceText = view.findViewById<EditText>(R.id.etRawPrice).text.toString().trim()

            if (title.isNotEmpty() && priceText.isNotEmpty()) {
                val price = priceText.toDoubleOrNull()
                if (price != null) {
                    rawMaterials.add(mapOf("title" to title, "price" to price))
                }
            }
        }

        val idea = hashMapOf(
            "name" to name,
            "description" to description,
            "category" to category,
            "min_budget" to minBudget,
            "max_budget" to maxBudget,
            "raw_materials" to rawMaterials
        )

        // Save data into Firestore
        firestore.collection("business_ideas")
            .add(idea)
            .addOnSuccessListener {
                Toast.makeText(this, "Idea Added Successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /** Function to clear fields after successful submission */
    private fun clearFields() {
        etName.text.clear()
        etDescription.text.clear()
        etCategory.text.clear()
        etMinBudget.text.clear()
        etMaxBudget.text.clear()
        rawMaterialsContainer.removeAllViews()
        rawMaterialViews.clear()
        addRawMaterialBox() // Add one empty field back
    }
}
