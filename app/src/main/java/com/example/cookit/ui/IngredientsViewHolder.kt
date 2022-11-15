package com.example.cookit.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.R

class IngredientsViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView){
    var icon : ImageView = itemView.findViewById(R.id.ingredientIcon)
    val ingredient : TextView = itemView.findViewById(R.id.ingredientName)
    val quantity : TextView = itemView.findViewById(R.id.ingredientQuantity)
    val unit : TextView = itemView.findViewById(R.id.ingredientUnit)
}