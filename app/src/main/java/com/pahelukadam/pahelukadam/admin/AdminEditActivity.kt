package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.R

class AdminEditActivity : AppCompatActivity() {

    private lateinit var etBusinessName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etCategory: EditText
    private lateinit var etBudgetMin: EditText
    private lateinit var etBudgetMax: EditText
    private lateinit var btnModify: Button
    private lateinit var btnRemove: Button
    private lateinit var btnAddRawMaterial: ImageButton
    private lateinit var rawMaterialsContainer: LinearLayout

    private val db = FirebaseFirestore.getInstance()
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit)

        // Initialize all views
        etBusinessName = findViewById(R.id.etBusinessName)
        etDescription = findViewById(R.id.etDescription)
        etCategory = findViewById(R.id.etCategory)
        etBudgetMin = findViewById(R.id.etBudgetMin)
        etBudgetMax = findViewById(R.id.etBudgetMax)
        btnModify = findViewById(R.id.btnModify)
        btnRemove = findViewById(R.id.btnRemove)
        btnAddRawMaterial = findViewById(R.id.btnAddRawMaterial)
        rawMaterialsContainer = findViewById(R.id.rawMaterialsContainer)

        documentId = intent.getStringExtra("docId")

        // Fetch all details for this business idea from Firestore
        fetchBusinessIdeaDetails()

        // Set up listeners for the main buttons
        setupButtonListeners()
    }

    /**
     * Fetches the entire document from Firestore to populate all fields,
     * including the existing raw materials.
     */
    private fun fetchBusinessIdeaDetails() {
        if (documentId == null) {
            Toast.makeText(this, "Error: Document ID not found.", Toast.LENGTH_SHORT).show()
            finish() // Can't edit without an ID, so close the activity
            return
        }

        db.collection("business_ideas").document(documentId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Populate the main EditText fields
                    etBusinessName.setText(document.getString("businessName"))
                    etDescription.setText(document.getString("description"))
                    etCategory.setText(document.getString("category_name"))

                    // Split the budget_range string and populate min/max fields
                    val budgetRange = document.getString("budget_range")?.split(" - ") ?: listOf()
                    etBudgetMin.setText(budgetRange.getOrNull(0) ?: "")
                    etBudgetMax.setText(budgetRange.getOrNull(1) ?: "")

                    // Populate existing raw materials
                    val rawMaterials = document.get("rawMaterials") as? List<Map<String, Any>>
                    if (rawMaterials != null && rawMaterials.isNotEmpty()) {
                        for (material in rawMaterials) {
                            val title = material["title"] as? String
                            val price = (material["price"] as? Number)?.toDouble()
                            addRawMaterialBox(title, price) // Add a pre-filled box
                        }
                    } else {
                        // If no raw materials exist, add one empty box to start
                        addRawMaterialBox(null, null)
                    }
                } else {
                    Toast.makeText(this, "Error: Document not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Inflates a raw material box. It can be empty (for new items) or
     * pre-filled with existing data. It also sets up the remove button.
     */
    private fun addRawMaterialBox(title: String?, price: Double?) {
        val inflater = LayoutInflater.from(this)
        val boxView = inflater.inflate(R.layout.item_raw_material, rawMaterialsContainer, false)

        val etTitle = boxView.findViewById<EditText>(R.id.etRawTitle)
        val etPrice = boxView.findViewById<EditText>(R.id.etRawPrice)
        val btnRemoveBox = boxView.findViewById<ImageButton>(R.id.btnRemoveRawMaterial)

        // Pre-fill the fields if data was passed
        title?.let { etTitle.setText(it) }
        price?.let { etPrice.setText(it.toString()) }

        // Set the listener for the remove ('X') button on this specific box
        btnRemoveBox.setOnClickListener {
            rawMaterialsContainer.removeView(boxView)
        }

        rawMaterialsContainer.addView(boxView)
    }

    /**
     * Collects the data from all currently displayed raw material boxes
     * and returns it as a list of maps, ready for Firestore.
     */
    private fun getRawMaterialsData(): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        for (i in 0 until rawMaterialsContainer.childCount) {
            val boxLayout = rawMaterialsContainer.getChildAt(i)
            val etTitle = boxLayout.findViewById<EditText>(R.id.etRawTitle)
            val etPrice = boxLayout.findViewById<EditText>(R.id.etRawPrice)

            val title = etTitle.text.toString()
            val priceStr = etPrice.text.toString()

            if (title.isNotEmpty() && priceStr.isNotEmpty()) {
                list.add(
                    mapOf(
                        "title" to title,
                        "price" to (priceStr.toDoubleOrNull() ?: 0.0)
                    )
                )
            }
        }
        return list
    }

    /**
     * Sets up the listeners for the main buttons on the page.
     */
    private fun setupButtonListeners() {
        // Listener to add a new, empty raw material box
        btnAddRawMaterial.setOnClickListener {
            addRawMaterialBox(null, null)
        }

        // Listener for the "MODIFY IDEA" button
        btnModify.setOnClickListener {
            val budgetMin = etBudgetMin.text.toString()
            val budgetMax = etBudgetMax.text.toString()
            val budgetRange = "$budgetMin - $budgetMax"

            val updatedIdea = hashMapOf(
                "businessName" to etBusinessName.text.toString(),
                "description" to etDescription.text.toString(),
                "category_name" to etCategory.text.toString(),
                "budget_range" to budgetRange,
                "rawMaterials" to getRawMaterialsData() // This gets all current items
            )

            documentId?.let { id ->
                db.collection("business_ideas").document(id)
                    .update(updatedIdea as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Idea Updated Successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Listener for the "REMOVE IDEA" button
        btnRemove.setOnClickListener {
            documentId?.let {
                db.collection("business_ideas").document(it)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Idea Removed!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}