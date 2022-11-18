package com.example.cookit

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookit.data.MainRepository
import com.example.cookit.data.RoomDataBase
import com.example.cookit.models.Recipe
import com.example.cookit.models.RecipeDao
import com.example.cookit.models.RecipeDetailModel
import com.example.cookit.models.RecipeEntity
import com.example.cookit.ui.RecipeAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class Home : AppCompatActivity() {
    private val coroutineContext : CoroutineContext = newSingleThreadContext("main")
    private val recipeQueryContext : CoroutineContext = newSingleThreadContext("query")
    private val  scope = CoroutineScope(coroutineContext)
    private val  scopeQuery = CoroutineScope(recipeQueryContext)

    private lateinit var rvRecipes : RecyclerView
    private var recipes = ArrayList<Recipe>()
//    private var recetaDetail : RecipeDetailModel? = null
    private lateinit var adapter : RecipeAdapter
//    private lateinit var adapterEntity : RecipeEntityAdapter
    private lateinit var btnFavourite : ImageButton
    private lateinit var tvUser : TextView
//    private lateinit var searchView: SearchView
    private lateinit var searchInput: EditText

    // firebase auth
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private lateinit var btnLogout : Button
    private lateinit var btnLogout : ImageButton

    // google logout
    lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        val recycler = findViewById<RecyclerView>(R.id.rvRecipes)
//        val adapter = RecipeAdapter(recipes, this)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        initRecyclerView()
        onClickDetails()
        agregarFavorito()
        eliminarFavorito()
//        buscarRecetas()

        searchInput = findViewById(R.id.searchInput)

        btnFavourite = findViewById(R.id.btnFavourite)
        btnFavourite.setOnClickListener{
            cambioPantallaFavoritos()
        }

        checkUser()

        // handle click -> logout user
        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                firebaseAuth.signOut()
                checkUser()
            }
        }

        // Configure the Google Sign OUT
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun checkUser() {
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // user not logged in
            Log.d("Login", "Usuario no registrado")
            startActivity(Intent(this@Home, Login::class.java))
            finish()
        }
        else {
            // user logueado
            // get user email
            val email = firebaseUser.email
            // get user
            var user = firebaseUser.displayName
            user = splitName(user!!)
            // set email
//            Log.d("Login", "checkUser: ${user}")
            tvUser = findViewById(R.id.tvUser)
            tvUser.text = user;
//             set name

        }
    }

    private fun splitName(user: String): String {
        return user.split(Regex(" "))[0]
    }

    private fun initRecyclerView() {
        rvRecipes = findViewById<RecyclerView>(R.id.rvRecipes)
        rvRecipes.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false) // cambio a un layout GRID buscar como funciona
        adapter = RecipeAdapter(recipes, this)
        rvRecipes.adapter = adapter
    }

    private fun onClickDetails() {
        adapter.onItemClick = { recipe : Recipe ->
            scope.launch {
                val receta = MainRepository.getRecipebyID(this@Home, recipe.id)
                showRecipeDetails(receta)
            }
        }
    }

    private fun showRecipeDetails(receta: RecipeDetailModel) {
        val intent = Intent (this@Home, RecipeDetail::class.java)
        intent.putExtra("title", receta.title)
        intent.putExtra("img", receta.image)
        intent.putExtra("ingredients", receta.ingredients)
//        intent.putExtra("instruccions", receta.summary)
        startActivity(intent)
    }

    private fun agregarFavorito() {
        val user = firebaseAuth.currentUser!!.email
        Log.d("Favourite", "agregarFavorito: email HOME agregarFavorito ${user}")
        adapter.onItemFavouriteClick = { recipe : Recipe ->
            scope.launch {
                MainRepository.insertRecipeFavourite(this@Home, RecipeEntity(recipe.id,
                    user!!, recipe.title, recipe.image, true), user)
            }
        }
    }

    private fun eliminarFavorito() {
        val user = firebaseAuth.currentUser!!.email
        adapter.onItemNOTFavouriteClick = { recipe : Recipe ->
            scope.launch{
                MainRepository.deleteRecipeFromFavourite(this@Home, recipe.id, user!!)
                recipe.statusFav = false;
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser!!.email

        scope.launch {
//            Log.d("Favourite", "onStart email HOME: ${user}")
            val recipesRoom = RoomDataBase.getInstance(this@Home).recipeDao()
            recipesRoom.fetchAll(user!!)
//            Log.d("Favourite", "onStart: ${recipesRoom}")
            val recipesMain = MainRepository.getRecipes(this@Home, user)
//            Log.d("getrecetas OK", "llegamooos")
            recipes = verificarFavoritos(recipesRoom, recipesMain, user)
            withContext(Dispatchers.Main) {
                adapter.update(recipes)
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                btnLogout.visibility = View.INVISIBLE

            }

            override fun onTextChanged(search: CharSequence?, start: Int, before: Int, count: Int) {
//                btnLogout.visibility = View.INVISIBLE

                // uso las recetas que me traigo de la API por primera vez
                updateRecipesQueryFromRoom(recipes, search)

                // hago una consulta a la API con lo que se busca -> Trae mas cantidad de recetas
//                val recipesFound = MainRepository.getRecipesSearch(this@Home, user!!, search.toString())
//                updateRecipesQuery(recipesFound)
            }

            private fun updateRecipesQueryFromRoom( recipesDB: ArrayList<Recipe>, search: CharSequence?) {
                val recipesFound = ArrayList<Recipe>();
                for (recipe in recipesDB) {
                    if  (recipe.title.uppercase().contains(search.toString().uppercase())) {
                        recipesFound.add(recipe)
                    }
                }
                updateRecipesQuery(recipesFound);
            }

            private fun updateRecipesQuery(recipesFound: ArrayList<Recipe>) {
                scopeQuery.launch {
                    withContext(Dispatchers.Main) {
                        adapter.update(recipesFound)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
//                btnLogout.visibility = View.GONE

            }
        })
    }

    private suspend fun verificarFavoritos(recipesRoom: RecipeDao, recipes: ArrayList<Recipe>, user: String): ArrayList<Recipe> {
        for (recipe in recipes){
            val rr = recipesRoom.fetchAll(user)
            for (r in rr) {
                if (r.id == recipe.id){
                    recipe.statusFav = true;
                }
            }
        }
        return recipes;
    }

    private fun cambioPantallaFavoritos() {
        val intent = Intent(this, Favourite::class.java)
        intent.putExtra("Titulo", "Recetas guardadas")
        startActivity(intent)
    }

}