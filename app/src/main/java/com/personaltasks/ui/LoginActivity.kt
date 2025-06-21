package com.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.personaltasks.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val alb: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    val signInCotoutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        setSupportActionBar(alb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Login"


        alb.singUpBt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        alb.singInBt.setOnClickListener {
            val email = alb.emailLoginEt.text.toString()
            val password = alb.passwordLoginEt.text.toString()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
            else{
                signInCotoutineScope.launch {
                    Firebase.auth.signInWithEmailAndPassword(
                        email,
                        password
                    )
                    .addOnFailureListener {
                        Toast.makeText(this@LoginActivity, "Login failed. Cause: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnSuccessListener {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        openMainActivity()
                    }
                }
            }
        }

        alb.resetPasswordBt.setOnClickListener {
            signInCotoutineScope.launch {
                val email = alb.emailLoginEt.text.toString()
                if (email.isNotEmpty()){
                    Firebase.auth.sendPasswordResetEmail(email)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null){
            openMainActivity()
        }
    }

    private fun openMainActivity(){
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}