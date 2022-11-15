package com.example.cookit

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookit.models.Ingredient
import com.example.cookit.models.Ingredients
import com.example.cookit.ui.IngredientsAdapter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RecipeDetail : AppCompatActivity() {
    private val coroutineContext : CoroutineContext = newSingleThreadContext("detail")
    private val  scope = CoroutineScope(coroutineContext)

    val TAG = "Detail"

    private lateinit var rvIngredients : RecyclerView
    private lateinit var adapter : IngredientsAdapter

    private var ingredientsList = ArrayList<Ingredient>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = IngredientsAdapter(ingredientsList, this)

        setContentView(R.layout.activity_recipe_detail)
        supportActionBar?.hide()

        initRecyclerView()


        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }
    }





    override fun onStart() {
        super.onStart()

        val title = findViewById<TextView>(R.id.txtTituloReceta)
        Log.d(TAG, "onStart: ${title}")
        val img = findViewById<ImageView>(R.id.imgRecipe)
        Log.d(TAG, "onStart: ${img}")
        val quantityItems = findViewById<TextView>(R.id.tvItems)
        Log.d(TAG, "onStart: ${quantityItems}")

        scope.launch{
            ingredientsList = intent.getSerializableExtra("ingredients") as ArrayList<Ingredient>
            quantityItems.text = ingredientsList.size.toString() + " items"
            Log.d(TAG, "onStart: ${ingredientsList.size}")
            withContext(Dispatchers.Main) {
                adapter.update(ingredientsList)
            }
        }
//        val ingredients = findViewById<RecyclerView>(R.id.rvIngredients)

//        var ingredientsList = intent.getSerializableExtra("ingredients") as MutableList<Ingredient>

//        ingredients.layoutManager = LinearLayoutManager(this)
//        adapter = IngredientsAdapter(ingredientsList, this)
//        ingredients.adapter = adapter

//        var ingredients = intent.extras?.getStringArrayList("ingredients")
//        val ingredients = findViewById<ListView>(R.id.txtIngredients)
//        val instruccions = findViewById<TextView>(R.id.txtInstruccions)


        title.text = intent.extras?.getString("title")

        Glide.with(this)
            .load(intent.extras?.getString("img"))
            .placeholder(R.drawable.cargando)
            .centerCrop()
            .into(img)

//        ingredients.layoutManager = LinearLayoutManager(this)
//        adapter = IngredientsAdapter(this)
//        rvIngredients.adapter = adapter
//        initRecyclerView()


//        val ingList = intent.getSerializableExtra("ingredients") as MutableList<Ingredients>
//
//        val ingredientsName : ArrayList<String> = ArrayList()
//        for (ingredient in ingList) {
//            ingredientsName.add(ingredient.name)
//        }

//        val arrayAdapter  : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsName)
//        ingredients.adapter = arrayAdapter

//        instruccions.text = splitIngredientes(intent.extras?.getString("instruccions")!!)


    }

    fun initRecyclerView() {
        rvIngredients = findViewById<RecyclerView>(R.id.rvIngredients)
        rvIngredients.layoutManager = LinearLayoutManager(this)
        adapter = IngredientsAdapter(ingredientsList, this)
        rvIngredients.adapter = adapter
    }

    private fun splitIngredientes(instruccions: String): String {
        var result = ""
        result = instruccions
            .replace("<p>","")
            .replace("</p>", "")
            .replace("<a>","")
            .replace("</a>", "")
            .replace("<b>","")
            .replace("</b>", "")
            .replace("<ul>","")
            .replace("</ul>", "")
            .replace("<li>","")
            .replace("</li>", "")
            .replace("<em>","")
            .replace("</em>", "")
            .replace("<a","")
            .replace(">","")
            .replace("href=","")
        return result
    }
}