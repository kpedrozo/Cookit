package com.example.cookit.data

import com.example.cookit.models.RecipeDetailModel
import com.example.cookit.models.RecipeEntity
import com.example.cookit.models.ResponseAPI
import io.grpc.internal.SharedResourceHolder.Resource
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesAPI {

    @GET("/recipes/complexSearch")
    fun getRecipes (@Header("x-api-key") apiKey : String,
                    @Query("number") number : Int) : Call<ResponseAPI>;


    @GET("/recipes/{id}/information/")
    fun getRecipebyID (@Path("id") id : Int,
                       @Header ("x-api-key") apiKey : String)
            : Call<RecipeDetailModel>



}