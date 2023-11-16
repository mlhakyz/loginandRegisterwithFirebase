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

    lateinit var emailReg: EditText
    lateinit var passwordReg: EditText
    lateinit var emailLog: EditText
    lateinit var passwordLog: EditText
    lateinit var tvLoggedIn: TextView
    lateinit var btnRegister: Button
    lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailReg = findViewById(R.id.etEmailRegister)
        passwordReg = findViewById(R.id.etPasswordRegister)
        emailLog = findViewById(R.id.etEmailLogin)
        passwordLog = findViewById(R.id.etPasswordLogin)
        tvLoggedIn = findViewById(R.id.tvLoggedIn)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        auth = FirebaseAuth.getInstance()
auth.signOut()
        btnRegister.setOnClickListener {
            registerUser()
        }
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    private fun registerUser(){
        val email =   emailReg.text.toString()
        val password =   passwordReg.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email,password).await()
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
    private fun loginUser(){
        val emailLogin =   emailLog.text.toString()
        val passwordLogin =   passwordLog.text.toString()

        if(emailLogin.isNotEmpty() && passwordLogin.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(emailLogin,passwordLogin).await()
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
}