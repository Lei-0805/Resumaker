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


class skills : AppCompatActivity() {

    lateinit var et_skill : EditText
    lateinit var et_rate_skill : EditText
    lateinit var btn_save_skill : Button
    lateinit var ibtn_back9 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.skill)

        et_skill = findViewById(R.id.et_skill)
        et_rate_skill = findViewById(R.id.et_rate_skill)
        btn_save_skill = findViewById(R.id.btn_save_skill)
        ibtn_back9 = findViewById(R.id.ibtn_back9)

        btn_save_skill.setOnClickListener {
            if (areFieldsValid()) {
                val company = et_skill.text.toString()
                val job = et_rate_skill.text.toString()

                val rate: Int
                try {
                    rate = job.toInt()
                } catch (e: NumberFormatException) {
                    et_rate_skill.error = "Invalid Number"
                    return@setOnClickListener
                }

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
            }
        }

        ibtn_back9.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_skill.text.isNullOrEmpty() -> {
                et_skill.error = "Field is empty"
                false
            }

            et_skill.text.isNullOrEmpty() -> {
                et_skill.error = "Field is empty"
                false
            }

            !et_rate_skill.text.toString().matches(Regex("\\d+")) -> {
                et_rate_skill.error = "Invalid Number"
                false
            }

            else -> true
        }
    }
}