package com.example.cookit.ui

import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.R
import com.example.cookit.models.Recipe

class RecipeAdapter (var items: MutableList<Recipe>,
                     context: Context)
    : RecyclerView.Adapter<RecipeViewHolder>() {

    var onItemClick : ((Recipe) -> Unit)? = null
    var onItemFavouriteClick : ((Recipe) -> Unit)? = null
    var onItemNOTFavouriteClick : ((Recipe) -> Unit)? = null
    var onCLickFav : ((Recipe) -> Unit)? = null

    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.title.text = items[position].title

        Glide.with(context)
            .load(items[position].image.toString())
            .placeholder(R.drawable.cargando)
            .centerCrop()
            .into(holder.img)

        val item = items[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }

        if (item.statusFav) {
            holder.btnFavourite.backgroundTintList = getColorStateList(context, R.color.red_favourite)
            Log.d("Favourite", "onBindViewHolder: title : ${item.title} || status ${item.statusFav}")
        } else {
            holder.btnFavourite.backgroundTintList = getColorStateList(context, R.color.dark_gray)
        }

        val favourite = items[position]
        var flag = true;
        holder.btnFavourite.setOnClickListener {
            var status = favourite.statusFav;
            Log.d("Favourite", "onBindViewHolder: aca dependeria del status fav")
            if (status) {
                // si ya esta en favoritos deberia eliminarlo
                Log.d("Favourite", "onBindViewHolder: aca eliminamos al que esta en favoritos")
                holder.btnFavourite.backgroundTintList = getColorStateList(context, R.color.dark_gray)
                flag = false;
                onItemNOTFavouriteClick?.invoke(favourite)

            } else {
                // si NO ES FAVORITO agregarlo a favoritos
                Log.d("Favourite", "onBindViewHolder: Aca agregamos a favoritos")
                holder.btnFavourite.backgroundTintList = getColorStateList(context, R.color.red_favourite)
                flag = true;
                onItemFavouriteClick?.invoke(favourite)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(recipes_new: MutableList<Recipe>) {
        items = recipes_new
        this.notifyDataSetChanged()
    }

}