package com.example.cookit.models

data class Recipe (
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    var statusFav : Boolean
)
