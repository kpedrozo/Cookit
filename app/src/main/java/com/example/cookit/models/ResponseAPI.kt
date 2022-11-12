package com.example.cookit.models

data class ResponseAPI (
    val results : ArrayList<Recipe>,
    val offset : Int,
    val number: Int,
    val totalResults: Int
)