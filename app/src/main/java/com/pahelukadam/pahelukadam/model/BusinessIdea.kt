package com.pahelukadam.pahelukadam.model

data class BusinessIdea(
    val title: String = "",
    val category: String = "",
    val budget: Map<String, Long> = emptyMap()
)