package com.example.cookit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.data.MainRepository
import com.example.cookit.models.Recipe
import com.example.cookit.models.RecipeDetailModel
import com.example.cookit.models.RecipeEntity
import com.example.cookit.ui.RecipeEntityAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Favourite : AppCompatActivity() {
    private val coroutineContext : CoroutineContext = newSingleThreadContext("favourite")
    private val  scope = CoroutineScope(coroutineContext)
    private lateinit var rvRecipes : RecyclerView
    private var recipes = ArrayList<Recipe>()
    private var recipesEntities = ArrayList<RecipeEntity>()
    private lateinit var adapter : RecipeEntityAdapter

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser!!.email


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        initRecyclerView()
        onClickDetails()
        agregarFavorito()
        eliminarFavorito()

        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        btnHome.setOnClickListener{
            cambioPantallaHome()
        }
    }


    fun initRecyclerView() {
        rvRecipes = findViewById<RecyclerView>(R.id.rvRecipes)
        rvRecipes.layoutManager = LinearLayoutManager(this)
        adapter = RecipeEntityAdapter(this)
        rvRecipes.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            recipesEntities = MainRepository.getRecipesFromRoom(this@Favourite, user!!) as ArrayList<RecipeEntity>
            withContext(Dispatchers.Main) {
                adapter.update(recipesEntities)
            }

        }
    }

    private fun eliminarFavorito() {
        adapter.onItemNOTFavouriteClick = {
            scope.launch {
                MainRepository.deleteRecipeFromFavourite(this@Favourite, it.id, user!!)
            }
        }
    }

    private fun agregarFavorito() {
        adapter.onItemFavouriteClick = {
            scope.launch {
                MainRepository.insertRecipeFavourite(this@Favourite, it, user!! )
            }
        }
    }

    private fun onClickDetails() {
        adapter.onItemClick = {
            scope.launch {
                val receta = MainRepository.getRecipebyID(this@Favourite, it.id)
                showRecipeDetails(receta)
            }
        }
    }

    private fun showRecipeDetails(receta: RecipeDetailModel) {
        val intent = Intent (this@Favourite, RecipeDetail::class.java)
        intent.putExtra("title", receta.title)
        intent.putExtra("img", receta.image)
        intent.putExtra("ingredients", receta.ingredients)
        intent.putExtra("instruccions", receta.summary)
        startActivity(intent)
    }

    fun cambioPantallaHome () {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

}