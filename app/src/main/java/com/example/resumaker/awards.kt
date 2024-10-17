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


class awards : AppCompatActivity() {

    lateinit var et_achievement: EditText
    lateinit var et_year_receive: EditText
    lateinit var btn_save_award: Button
    lateinit var ibtn_back2: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.award)

        et_achievement = findViewById(R.id.et_achievement)
        et_year_receive = findViewById(R.id.et_year_receive)
        btn_save_award = findViewById(R.id.btn_save_award)
        ibtn_back2 = findViewById(R.id.ibtn_back2)

        btn_save_award.setOnClickListener {
            if (areFieldsValid()) {
                val achievement = et_achievement.text.toString()
                val year_receive = et_year_receive.text.toString()

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
        ibtn_back2.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun areFieldsValid(): Boolean {
        return when {
            et_achievement.text.isNullOrEmpty() -> {
                et_achievement.error = "Field is empty"
                false
            }
            et_year_receive.text.isNullOrEmpty() -> {
                et_year_receive.error = "Field is empty"
                false
                }
            else -> true
        }
    }
}
