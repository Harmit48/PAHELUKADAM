package com.pahelukadam.pahelukadam.utils

import android.content.Context
import android.util.Log
// import android.widget.Toast   // ⛔ Removed unused import
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.data.InitialBusinessData

object FirestoreSeeder {

    fun seedDatabase(context: Context) {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("business_ideas")

        // Fetch all existing docs first
        collectionRef.get()
            .addOnSuccessListener { snapshot ->
                val existingNames = snapshot.documents.mapNotNull { it.id } // IDs = businessName

                var addedCount = 0

                InitialBusinessData.businessIdeas.forEach { idea ->
                    val docId = idea.businessName ?: return@forEach

                    if (existingNames.contains(docId)) {
                        Log.d("FirestoreSeeder", "Skipping (already exists): $docId")
                    } else {
                        collectionRef.document(docId).set(idea)
                            .addOnSuccessListener {
                                addedCount++
                                Log.d("FirestoreSeeder", "Added: $docId")
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirestoreSeeder", "Error adding $docId", e)
                            }
                    }
                }

                // Toast.makeText(
                //     context,
                //     "Seeding complete. Added $addedCount new ideas.",
                //     Toast.LENGTH_LONG
                // ).show()
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreSeeder", "Error reading collection", e)
                // Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
