package com.example.cookit

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {


    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogin: SignInButton

    // constantes
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure the Google Sign in
        val gsiOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gsiOptions)

        // iniciar firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        btnLogin = findViewById(R.id.btnGoogleSignIn)

        reDesignGoogleButton(btnLogin, "Continuar con Google")

        // Click para comenzar el Google SignIn
        btnLogin.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, 100)
        }
    }

    private fun reDesignGoogleButton(btnLogin: SignInButton, btnText: String) {
            for (i in 0 until btnLogin.childCount) {
                val v = btnLogin.getChildAt(i)
                if (v is TextView) {
                    v.text = btnText
                    v.typeface = Typeface.create("@font/poppins_regular", Typeface.NORMAL )
                    return
                }
            }
    }

    private fun checkUser() {
        // check si el usuario esta logeado
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // Si el usuario esta logeado pasamos a Home
            startActivity(Intent(this@Login, Home::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the intent from Google SignInApi
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // SignIn Google OK -> intenta authenticar firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                // SignIn Google fallo
                Log.d(TAG, "onActivityResult - Error: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: comienza auth firebase con Google")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                // login success
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                // get usuario loggeado
                val firebaseUser = firebaseAuth.currentUser
                // get user info
                var uid = firebaseUser!!.uid
                var email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                // check si el usuario es nuevo o existente
                if (authResult.additionalUserInfo!!.isNewUser) {
                    // user NEW -> crea cuenta
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(this@Login, "Creando cuenta...", Toast.LENGTH_LONG).show()
                } else {
                    // user existente -> Logeado
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Usuario existente.. \n$email")
                    Toast.makeText(this@Login, "Hola ${firebaseUser.displayName!!.split(Regex(" "))[0]}!", Toast.LENGTH_SHORT).show()
                }
                // pasamos a pantalla Home
                startActivity(Intent(this@Login, Home::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin failed due to ${e.message}")
                Toast.makeText(this@Login, "Fallo en login", Toast.LENGTH_LONG).show()
            }
    }
}