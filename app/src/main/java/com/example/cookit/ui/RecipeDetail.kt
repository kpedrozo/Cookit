package com.example.cookit

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.example.cookit.models.Ingredients

class RecipeDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        val title = findViewById<TextView>(R.id.txtTituloReceta)
        val img = findViewById<ImageView>(R.id.imgRecipe)
        val ingredients = findViewById<ListView>(R.id.txtIngredients)
        val instruccions = findViewById<TextView>(R.id.txtInstruccions)


        title.text = intent.extras?.getString("title")

        Glide.with(this)
            .load(intent.extras?.getString("img"))
            .placeholder(R.drawable.cargando)
            .centerCrop()
            .into(img)


        val ingList = intent.getSerializableExtra("ingredients") as MutableList<Ingredients>

        val ingredientsName : ArrayList<String> = ArrayList()
        for (ingredient in ingList) {
            ingredientsName.add(ingredient.name)
        }

        val arrayAdapter  : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsName)
        ingredients.adapter = arrayAdapter

        instruccions.text = splitIngredientes(intent.extras?.getString("instruccions")!!)


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