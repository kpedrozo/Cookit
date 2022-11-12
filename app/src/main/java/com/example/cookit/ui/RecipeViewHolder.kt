package com.example.cookit.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView) {
    val img : ImageView = itemView.findViewById(R.id.imgRecipe)
    val title : TextView = itemView.findViewById(R.id.txtRecipeTitle)
    val btnFavourite : FloatingActionButton = itemView.findViewById(R.id.btnGuardarFavorito)

}