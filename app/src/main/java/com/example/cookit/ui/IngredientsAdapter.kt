package com.example.cookit.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.R
import com.example.cookit.models.Ingredient

class IngredientsAdapter (var items: MutableList<Ingredient>,
                          context: Context)
    : RecyclerView.Adapter<IngredientsViewHolder>() {

    private val context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_ingredient_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {

        holder.icon.setImageResource(R.drawable.ingredient)

        holder.ingredient.text = items[position].name

        //pasamos cantidad a INT para evitar DOUBLE
        val quantity = items[position].amount.toInt()
        holder.quantity.text = quantity.toString()

        holder.unit.text = items[position].unit
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(updatedItems : MutableList<Ingredient>) {
        items = updatedItems
        notifyDataSetChanged()
    }
}