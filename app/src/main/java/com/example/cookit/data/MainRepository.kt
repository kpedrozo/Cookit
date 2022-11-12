package com.example.cookit.data

import android.content.Context
import com.example.cookit.models.Recipe
import com.example.cookit.models.RecipeDetailModel
import com.example.cookit.models.RecipeEntity

class MainRepository {
    companion object {
        suspend fun getRecipes (context: Context, user: String) : ArrayList<Recipe> {
            return APIService.getRecipes(context, user)
        }

        suspend fun getRecipebyID (context: Context, id: Int?) : RecipeDetailModel {
            return APIService.getRecipebyID(context, id!!)
        }

        suspend fun getRecipesFromRoom (context: Context, user: String?) : MutableList<RecipeEntity> {
            return APIService.getRecipesFavourite(context, user!!);
        }

        suspend fun deleteRecipeFromFavourite (context: Context, id: Int?, user: String) {
            return APIService.deleteRecipeFavourite(context, id!!, user)
        }

        suspend fun insertRecipeFavourite (context: Context, recipe: RecipeEntity, user: String) {
            return APIService.insertRecipeFavourite(context, recipe, user)
        }

    }
}