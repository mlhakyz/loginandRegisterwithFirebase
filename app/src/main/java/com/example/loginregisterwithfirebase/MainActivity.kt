package com.example.loginregisterwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var tvLoggedIn: TextView
    lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = findViewById(R.id.etEmailRegister)
        password = findViewById(R.id.etPasswordRegister)
        tvLoggedIn = findViewById(R.id.tvLoggedIn)
        btnRegister = findViewById(R.id.btnRegister)
        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        val emailReg =   email.text.toString()
        val passwordReg =   password.text.toString()

        if(emailReg.isNotEmpty() && passwordReg.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(emailReg,passwordReg).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }

    private fun checkLoggedInState(){
        if (auth.currentUser == null){
            tvLoggedIn.text = "You are not Logged in"
        }else{
            tvLoggedIn.text = "You are Logged in"
        }

    }
}