package com.example.cookit.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.cookit.models.Recipe
import com.example.cookit.models.RecipeDetailModel
import com.example.cookit.models.RecipeEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.coroutines.CoroutineContext

class APIService {
    companion object {

        private val coroutineContext : CoroutineContext = newSingleThreadContext("room")
        private val  scope = CoroutineScope(coroutineContext)

        val BASE_URL = "https://api.spoonacular.com/"
        val apiKey = "502e29b08c5b48ee9b92ebd598c8ee8b"
        val cantRecetas = 100;  // cambiar cantidad de recetas pedidas

        val TAG = "Favourite"

        private val myDB = Firebase.firestore.collection("favoritos")

        fun getRecipes (context: Context, user: String) : ArrayList<Recipe>{
            Log.d(TAG, "getRecipes: getRecipes email usuario: ${user}")
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiEndpoint = retrofit.create(RecipesAPI::class.java)
            val result = apiEndpoint.getRecipes(apiKey, cantRecetas).execute()

            if (result.isSuccessful) {
                return result.body()!!.results
            } else {
                Log.e("Api-Service", "Error al comunicar con la API")
                return ArrayList<Recipe>()
            }
        }

        fun getRecipesSearch (context: Context, user: String, query : String) : ArrayList<Recipe> {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiEndpoint = retrofit.create(RecipesAPI::class.java)
            val result = apiEndpoint.getRecipesSearch(apiKey, query).execute()

            if (result.isSuccessful) {
                return result.body()!!.results
            } else {
                Log.e("Api-Service", "Error al comunicar con la API")
                return ArrayList<Recipe>()
            }
        }

        suspend fun insertRecipeFavourite (
            context: Context,
            recetaFavorita: RecipeEntity,
            user: String
        ) {

            val room = RoomDataBase.getInstance(context).recipeDao().insertRecipe(recetaFavorita)

            val receta = hashMapOf(
                "title" to recetaFavorita.title,
                "image" to recetaFavorita.img
            )
            myDB
                .document(user).collection("recetas")
                .document("${recetaFavorita.id}")
                .set(receta)
                .addOnSuccessListener {
                    Log.d(TAG, "insertRecipeFavourite: receta favorita guardada en Firestore")
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "insertRecipeFavourite: Error: Receta favorita sin guardar en Firestore", e)
                }
            return room;
        }

        suspend fun getRecipebyID (context: Context, id : Int) : RecipeDetailModel {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiEndpoint = retrofit.create(RecipesAPI::class.java)
            val result = apiEndpoint.getRecipebyID(id , apiKey).execute()

            return if (result.isSuccessful) {
                result.body()!!
            } else {
                val response = RecipeDetailModel(0,"","", ArrayList(),"")
                return response
            }
        }

        suspend fun getRecipesFavourite(context: Context, user: String): MutableList<RecipeEntity> {

            val recetasRoom = RoomDataBase.getInstance(context).recipeDao().fetchAll(user);
            if (recetasRoom.size == 0){
                myDB.document(user).collection("recetas").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val id = document.id
                            val title = document.data["title"]
                            val img = document.data["image"]
                            scope.launch {
                            RoomDataBase.getInstance(context).recipeDao().insertRecipe(RecipeEntity(
                                Integer.parseInt(id), user, title as String, img as String, true
                            ))}
                    }
                }
            }
            return RoomDataBase.getInstance(context).recipeDao().fetchAll(user);
        }

        suspend fun deleteRecipeFavourite(context: Context, id: Int, user: String) {
            val room = RoomDataBase.getInstance(context).recipeDao().deleteByID(id, user)

            myDB.document(user).collection("recetas")
                .document("$id")
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "insertRecipeFavourite: receta favorita ELIMINADA de Firestore")
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "insertRecipeFavourite: Error: Receta favorita sin borrar de Firestore", e)
                }
            return room;
        }

        suspend fun selectRecipeFavouriteByID(context: Context, id: Int): RecipeEntity {
            val room = RoomDataBase.getInstance(context).recipeDao().selectByID(id)
            myDB.document("$id")
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "selectRecipeFavouriteByID: get receta favorita by ID desde Firestore")
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "selectRecipeFavouriteByID: error al buscar receta por id en Firestore", e)
                }
            return room;
        }
    }
}