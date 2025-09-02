package com.pahekukadam.pahelukadam.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.pahekukadam.pahelukadam.R

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

        // Get data from Intent
        documentId = intent.getStringExtra("docId")
        etBusinessName.setText(intent.getStringExtra("businessName"))
        etDescription.setText(intent.getStringExtra("description"))
        etCategory.setText(intent.getStringExtra("categoryID"))
        etBudgetMin.setText(intent.getStringExtra("budgetMin"))
        etBudgetMax.setText(intent.getStringExtra("budgetMax"))

        // Add first box by default
        addRawMaterialBox()

        // Add new box on "+" click
        btnAddRawMaterial.setOnClickListener {
            addRawMaterialBox()
        }

        // Save Updated Data
        btnModify.setOnClickListener {
            val updatedIdea = hashMapOf(
                "businessName" to etBusinessName.text.toString(),
                "description" to etDescription.text.toString(),
                "categoryID" to etCategory.text.toString(),
                "budgetID" to "${etBudgetMin.text} – ${etBudgetMax.text}",
                "rawMaterials" to getRawMaterialsData()
            )

            documentId?.let {
                db.collection("business_ideas").document(it)
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

        // Delete Idea
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

    /** Dynamically Inflate Raw Material Box **/
    private fun addRawMaterialBox() {
        val inflater = LayoutInflater.from(this)
        val boxView = inflater.inflate(R.layout.item_raw_material, rawMaterialsContainer, false)
        rawMaterialsContainer.addView(boxView)
    }

    /** Collect All Entered Raw Materials **/
    private fun getRawMaterialsData(): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        for (i in 0 until rawMaterialsContainer.childCount) {
            val boxLayout = rawMaterialsContainer.getChildAt(i)
            val etTitle = boxLayout.findViewById<EditText>(R.id.etRawTitle)
            val etPrice = boxLayout.findViewById<EditText>(R.id.etRawPrice)

            if (etTitle.text.isNotEmpty() && etPrice.text.isNotEmpty()) {
                list.add(
                    mapOf(
                        "title" to etTitle.text.toString(),
                        "price" to etPrice.text.toString().toDouble()
                    )
                )
            }
        }
        return list
    }
}
