package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class education : AppCompatActivity() {

    lateinit var et_jhs : EditText
    lateinit var et_schoolyear_jhs : EditText
    lateinit var et_shs : EditText
    lateinit var et_schoolyear_shs : EditText
    lateinit var et_program : EditText
    lateinit var et_school : EditText
    lateinit var et_schoolyear_college : EditText
    lateinit var btn_save_educ : Button
    lateinit var ibtn_back3 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.education)

        et_jhs = findViewById(R.id.et_jhs)
        et_schoolyear_jhs = findViewById(R.id.et_schoolyear_jhs)
        et_shs = findViewById(R.id.et_shs)
        et_schoolyear_shs = findViewById(R.id.et_schoolyear_shs)
        et_program = findViewById(R.id.et_program)
        et_school = findViewById(R.id.et_school)
        et_schoolyear_college = findViewById(R.id.et_schoolyear_college)
        btn_save_educ = findViewById(R.id.btn_save_educ)
        ibtn_back3 = findViewById(R.id.ibtn_back3)

        btn_save_educ.setOnClickListener {
            if (areFieldsValid()) {
                val jhs = et_jhs.text.toString()
                val schoolyear_jhs = et_schoolyear_jhs.text.toString()
                val shs = et_shs.text.toString()
                val schoolyear_shs = et_schoolyear_shs.text.toString()
                val program = et_program.text.toString()
                val school = et_school.text.toString()
                val schoolyear_college = et_schoolyear_college.text.toString()

                val intent = Intent(this, navfunction::class.java)
                startActivity(intent)
            }
        }

        ibtn_back3.setOnClickListener{
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_jhs.text.isNullOrEmpty() -> {
                et_jhs.error = "Field is empty"
                false
            }
            et_schoolyear_jhs.text.isNullOrEmpty() -> {
                et_schoolyear_jhs.error = "Field is empty"
                false
            }
            et_shs.text.isNullOrEmpty() -> {
                et_shs.error = "Field is empty"
                false
            }
            et_schoolyear_shs.text.isNullOrEmpty() -> {
                et_schoolyear_shs.error = "Field is empty"
                false
            }
            et_program.text.isNullOrEmpty() -> {
                et_program.error = "Field is empty"
                false
            }
            et_school.text.isNullOrEmpty() -> {
                et_school.error = "Field is empty"
                false
            }
            et_schoolyear_college.text.isNullOrEmpty() -> {
                et_schoolyear_college.error = "Field is empty"
                false
            }
            else -> true
        }
    }
}
