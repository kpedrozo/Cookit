package com.example.cookit.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.models.RecipeEntity
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import com.example.cookit.R


class RecipeEntityAdapter (val context: Context) : RecyclerView.Adapter<RecipeViewHolder>() {

    var onItemClick : ((RecipeEntity) -> Unit)? = null
    var onItemNOTFavouriteClick : ((RecipeEntity) -> Unit)? = null
    var onItemFavouriteClick : ((RecipeEntity) -> Unit)? = null


    private var items : MutableList<RecipeEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_favourite_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.title.text = items[position].title

        Glide.with(context)
            .load(items[position].img.toString())
            .placeholder(R.drawable.cargando)
            .centerCrop()
            .into(holder.img)

        val item = items[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }

        val favourite = items[position]
        var flag = true;
        holder.btnFavourite.setOnClickListener {
            Log.d("Favourite", "onBindViewHolder: marcado como favorito")
            if (flag) {
                // pasar a false y sacar de favoritos
                Log.d("Favourite", "onBindViewHolder: eliminar de favoritos")
                item.statusFav = false;
                holder.btnFavourite.backgroundTintList = getColorStateList(context, android.R.color.darker_gray)
                onItemNOTFavouriteClick?.invoke(favourite)
                flag = false;
            } else {
                // pasar a favoritos de nuevo
                Log.d("Favourite", "onBindViewHolder: guarda en favoritos de nuevo")
                item.statusFav = true;
                holder.btnFavourite.backgroundTintList = getColorStateList(context, android.R.color.holo_red_dark);
                flag = true;
                onItemFavouriteClick?.invoke(favourite)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(updatedItems : MutableList<RecipeEntity>) {
        items = updatedItems
        notifyDataSetChanged()
    }
}