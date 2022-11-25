package com.example.cookit.models

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "recipes", primaryKeys = ["id", "user"])
data class RecipeEntity (
    @ColumnInfo(name = "id") val id : Int,
    @ColumnInfo(name = "user") val user : String,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "img") val img : String,
    @ColumnInfo(name = "statusFav") var statusFav : Boolean,
    @ColumnInfo(name = "ttl") var ttl : String
)