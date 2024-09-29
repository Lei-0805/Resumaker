package com.example.resumaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var et_username1 : EditText
    lateinit var et_create_password : EditText
    lateinit var et_confirm_password : EditText
    lateinit var btn_signup : Button
    lateinit var tv_login : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signin)

        et_username1 = findViewById(R.id.et_username1)
        et_create_password = findViewById(R.id.et_create_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        btn_signup = findViewById(R.id.btn_signup)
        tv_login = findViewById(R.id.tv_login)

        btn_signup.setOnClickListener{
            val username = et_username1.text.toString()
            val create_password = et_create_password.text.toString()
            val confirm_password = et_confirm_password.text.toString()
            Log.i("Test Credentials","Username : $username and Create Password : $create_password and Confirm Password : $confirm_password")

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}