package com.pahelukadam.pahelukadam.model

// Add a no-argument constructor for Firestore deserialization
data class BestIdea(
    val businessName: String = "",
    val category_name: String = "",
    val budget_range: String = ""
)