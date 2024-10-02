package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class sign_in : AppCompatActivity() {

    lateinit var et_username1: EditText
    lateinit var et_create_password: EditText
    lateinit var et_confirm_password: EditText
    lateinit var btn_signup1: Button
    lateinit var btn_login2: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.signin)

        et_username1 = findViewById(R.id.et_username1)
        et_create_password = findViewById(R.id.et_create_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        btn_signup1 = findViewById(R.id.btn_signup1)
        btn_login2 = findViewById(R.id.btn_login2)

        btn_signup1.setOnClickListener {
            val username = et_username1.text.toString()
            val create_password = et_create_password.text.toString()
            val confirm_password = et_confirm_password.text.toString()

            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        btn_login2.setOnClickListener {
            val intent = Intent(this, log_in::class.java)
            startActivity(intent)
        }
    }
}