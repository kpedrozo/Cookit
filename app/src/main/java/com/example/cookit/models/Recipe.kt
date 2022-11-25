package com.example.cookit.models

import java.time.LocalDateTime

data class Recipe (
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    var statusFav : Boolean,
    var ttl : LocalDateTime
)
