package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class log_in : AppCompatActivity() {

    lateinit var et_username2: EditText
    lateinit var et_password: EditText
    lateinit var btn_login1: Button
    lateinit var btn_signup2: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        et_username2 = findViewById(R.id.et_username2)
        et_password = findViewById(R.id.et_password)
        btn_login1 = findViewById(R.id.btn_login1)
        btn_signup2 = findViewById(R.id.btn_signup2)

        btn_login1.setOnClickListener {
            val username = et_username2.text.toString()
            val password = et_password.text.toString()

            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        btn_signup2.setOnClickListener {
            val intent = Intent(this, sign_in::class.java)
            startActivity(intent)
        }
    }
}