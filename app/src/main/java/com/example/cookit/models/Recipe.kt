package com.example.cookit.models

data class Recipe (
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,



    // agregregue status para setear los q son favoritos
    var statusFav : Boolean
)
