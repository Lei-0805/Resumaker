package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class createdprofiles : AppCompatActivity() {

    private lateinit var btn_edit : Button
    private lateinit var btn_delete : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createdprofiles)

        btn_edit = findViewById(R.id.btn_edit)
        btn_delete = findViewById(R.id.btn_delete)

        btn_edit.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }
}

