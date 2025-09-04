package com.pahelukadam.pahelukadam.model

data class Adminbusinessidea(
    var id: String? = null,
    var businessName: String? = null,
    var description: String? = null,
    var category_name: String? = null,
    var budget_range: String? = null,
    var rawMaterials: List<Map<String, Any>>? = null
)
