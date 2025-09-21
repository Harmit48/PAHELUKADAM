package com.pahelukadam.pahelukadam.model

// Firestore needs default values
data class BestIdea(
    var id: String = "",              // ✅ Firestore document ID
    val businessName: String = "",
    val category_name: String = "",
    val budget_range: String = ""
)
