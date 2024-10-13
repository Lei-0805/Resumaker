package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class objective : AppCompatActivity() {

    lateinit var et_obj: EditText
    lateinit var btn_save_obj: Button
    lateinit var btn_add_obj: Button
    lateinit var ibtn_back5: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.objective)

        et_obj = findViewById(R.id.et_obj)
        btn_save_obj = findViewById(R.id.btn_save_obj)
        ibtn_back5 = findViewById(R.id.ibtn_back5)

        btn_save_obj.setOnClickListener {
            if (areFieldsValid()) {
                val company = et_obj.text.toString()

                val intent = Intent(this, navfunction::class.java)
                startActivity(intent)
            }
        }
        ibtn_back5.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_obj.text.isNullOrEmpty() -> {
                et_obj.error = "Field is empty"
                false
            }

            else -> true
        }
    }
}