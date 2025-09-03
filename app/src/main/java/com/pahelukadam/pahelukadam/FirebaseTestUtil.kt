package com.pahelukadam.pahelukadam

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FirebaseTestUtil {

    fun testConnection(context: Context) {
        try {
            val db = Firebase.firestore
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(Date())

            val statusData = mapOf(
                "status" to "Success",
                "last_checked" to timestamp
            )

            db.collection("status_logs").document("latest_connection").set(statusData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully wrote status to Firestore!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to write to Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("FirestoreTest", "DATABASE WRITE FAILED", e)
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Firestore initialisation error: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("FirestoreTest", "INITIALISATION FAILED", e)
        }
    }
}