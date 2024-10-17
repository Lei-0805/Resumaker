package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class project : AppCompatActivity() {

    lateinit var et_proj_title : EditText
    lateinit var et_proj_description : EditText
    lateinit var btn_save_projects : Button
    lateinit var ibtn_back7 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project)

        et_proj_title = findViewById(R.id.et_proj_title)
        et_proj_description = findViewById(R.id.et_proj_description)
        btn_save_projects = findViewById(R.id.btn_save_projects)
        ibtn_back7 = findViewById(R.id.ibtn_back7)


        btn_save_projects.setOnClickListener {
            if (areFieldsValid()) {
                val company = et_proj_title.text.toString()
                val job = et_proj_description.text.toString()

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
            }
        }

        ibtn_back7.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_proj_title.text.isNullOrEmpty() -> {
                et_proj_title.error = "Field is empty"
                false
            }
            et_proj_description.text.isNullOrEmpty() -> {
                et_proj_description.error = "Field is empty"
                false
            }
            else -> true
        }
    }
}
