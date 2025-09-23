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

        fetchBusinessIdeaDetails()
        setupButtonListeners()
    }

    private fun fetchBusinessIdeaDetails() {
        if (documentId == null) {
            Toast.makeText(this, "Error: Document ID not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        db.collection("business_ideas").document(documentId!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    etBusinessName.setText(document.getString("businessName"))
                    etDescription.setText(document.getString("description"))
                    etCategory.setText(document.getString("category_name"))

                    val budgetRange = document.getString("budget_range")?.split(" - ") ?: listOf()
                    etBudgetMin.setText(budgetRange.getOrNull(0) ?: "")
                    etBudgetMax.setText(budgetRange.getOrNull(1) ?: "")

                    val rawMaterials = document.get("rawMaterials") as? List<Map<String, Any>>
                    if (rawMaterials != null && rawMaterials.isNotEmpty()) {
                        for (material in rawMaterials) {
                            val title = material["title"] as? String
                            val price = (material["price"] as? Number)?.toDouble()
                            addRawMaterialBox(title, price)
                        }
                    } else {
                        addRawMaterialBox(null, null)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addRawMaterialBox(title: String?, price: Double?) {
        val inflater = LayoutInflater.from(this)
        val boxView = inflater.inflate(R.layout.item_raw_material, rawMaterialsContainer, false)

        val etTitle = boxView.findViewById<EditText>(R.id.etRawTitle)
        val etPrice = boxView.findViewById<EditText>(R.id.etRawPrice)
        val btnRemoveBox = boxView.findViewById<ImageButton>(R.id.btnRemoveRawMaterial)

        title?.let { etTitle.setText(it) }
        price?.let { etPrice.setText(it.toString()) }

        btnRemoveBox.setOnClickListener { rawMaterialsContainer.removeView(boxView) }
        rawMaterialsContainer.addView(boxView)
    }

    private fun getRawMaterialsData(): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        for (i in 0 until rawMaterialsContainer.childCount) {
            val boxLayout = rawMaterialsContainer.getChildAt(i)
            val etTitle = boxLayout.findViewById<EditText>(R.id.etRawTitle)
            val etPrice = boxLayout.findViewById<EditText>(R.id.etRawPrice)

            val title = etTitle.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()

            // ✅ Validation for raw materials
            if (title.isEmpty()) {
                etTitle.error = "Material name required"
                return emptyList()
            }
            if (priceStr.isEmpty()) {
                etPrice.error = "Price required"
                return emptyList()
            }

            val price = priceStr.toDoubleOrNull()
            if (price == null || price <= 0) {
                etPrice.error = "Enter valid price"
                return emptyList()
            }

            list.add(mapOf("title" to title, "price" to price))
        }
        return list
    }

    private fun setupButtonListeners() {
        btnAddRawMaterial.setOnClickListener { addRawMaterialBox(null, null) }

        btnModify.setOnClickListener {
            val name = etBusinessName.text.toString().trim()
            val desc = etDescription.text.toString().trim()
            val category = etCategory.text.toString().trim()
            val minBudget = etBudgetMin.text.toString().trim()
            val maxBudget = etBudgetMax.text.toString().trim()

            // ✅ Main field validation
            when {
                name.isEmpty() -> {
                    etBusinessName.error = "Business name required"
                    return@setOnClickListener
                }
                desc.isEmpty() -> {
                    etDescription.error = "Description required"
                    return@setOnClickListener
                }
                category.isEmpty() -> {
                    etCategory.error = "Category required"
                    return@setOnClickListener
                }
                minBudget.isEmpty() -> {
                    etBudgetMin.error = "Minimum budget required"
                    return@setOnClickListener
                }
                maxBudget.isEmpty() -> {
                    etBudgetMax.error = "Maximum budget required"
                    return@setOnClickListener
                }
            }

            // ✅ Budget validation
            val minVal = minBudget.toDoubleOrNull()
            val maxVal = maxBudget.toDoubleOrNull()
            if (minVal == null || minVal <= 0) {
                etBudgetMin.error = "Enter valid minimum budget"
                return@setOnClickListener
            }
            if (maxVal == null || maxVal <= 0) {
                etBudgetMax.error = "Enter valid maximum budget"
                return@setOnClickListener
            }
            if (minVal > maxVal) {
                etBudgetMax.error = "Max must be greater than Min"
                return@setOnClickListener
            }

            // ✅ Raw materials validation
            val rawMaterials = getRawMaterialsData()
            if (rawMaterials.isEmpty()) return@setOnClickListener

            val budgetRange = "$minBudget - $maxBudget"

            val updatedIdea = hashMapOf(
                "businessName" to name,
                "description" to desc,
                "category_name" to category,
                "budget_range" to budgetRange,
                "rawMaterials" to rawMaterials
            )

            documentId?.let { id ->
                db.collection("business_ideas").document(id)
                    .update(updatedIdea as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Idea Updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

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
