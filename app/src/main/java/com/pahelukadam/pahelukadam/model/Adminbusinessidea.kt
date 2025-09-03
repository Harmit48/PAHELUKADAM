package com.pahelukadam.pahelukadam.model

import com.google.firebase.firestore.DocumentId

data class Adminbusinessidea(
    // This annotation automatically populates the document ID from Firestore
    @DocumentId
    val id: String? = null,

    // The rest of your fields
    val budget_range: String? = null,
    val businessName: String? = null,
    val category_name: String? = null,
    val description: String? = null
)