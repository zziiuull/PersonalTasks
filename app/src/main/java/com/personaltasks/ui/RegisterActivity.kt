package com.personaltasks.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.personaltasks.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val arb: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arb.root)

        setSupportActionBar(arb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Register"

        arb.signUpBt.setOnClickListener {
            val email = arb.emailRegisterEt.text.toString()
            val password = arb.passwordRegisterEt.text.toString()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this@RegisterActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
            else {
                val signUpCoroutineScope = CoroutineScope(Dispatchers.IO)
                signUpCoroutineScope.launch {
                    Firebase.auth.createUserWithEmailAndPassword(
                        email,
                        password
                    ).addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, "Registration failed. Cause: ${it.message}", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            }
        }
    }
}