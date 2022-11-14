package com.example.cookit.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.R
import com.example.cookit.models.Ingredient
import com.example.cookit.models.Ingredients
import com.example.cookit.models.RecipeEntity

class IngredientsAdapter (val context: Context) : RecyclerView.Adapter<IngredientsViewHolder>() {

    private var items : MutableList<Ingredient> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ingedient_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {

        Glide.with(context)
            .load(items[position])
            .placeholder(R.drawable.favourite)
            .centerCrop()
            .into(holder.icon)

        holder.ingredient.text = items[position].name

        //REVISAR EL TEMA DE LA CANTIDAD PARA QUE LLEGUE OK!!
        holder.quantity.text = items[position].quantity
        TODO()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(updatedItems : MutableList<Ingredient>) {
        items = updatedItems
        notifyDataSetChanged()
    }
}