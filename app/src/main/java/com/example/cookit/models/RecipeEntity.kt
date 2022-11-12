package com.example.cookit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes", primaryKeys = ["id", "user"])
data class RecipeEntity (
//    @PrimaryKey
    @ColumnInfo(name = "id") val id : Int,
//    @PrimaryKey
    @ColumnInfo(name = "user") val user : String,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "img") val img : String,
    @ColumnInfo(name = "statusFav") var statusFav : Boolean,
    // @ColumnInfo(name = "imageType") val imageType: String
)