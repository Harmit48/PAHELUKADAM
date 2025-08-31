package com.pahekukadam.pahelukadam.model

data class BusinessIdea(
    val title: String = "",
    val category: String = "",
    val budget: Budget = Budget()
)

data class Budget(
    val min: Int = 0,
    val max: Int = 0
)
