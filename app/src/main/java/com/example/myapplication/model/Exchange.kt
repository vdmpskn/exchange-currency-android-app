package com.example.myapplication.model

data class Exchange(
    val from: Balance,
    val to: Balance,
    val rate: Double,
    val commission: Commission
)