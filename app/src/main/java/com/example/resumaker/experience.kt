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


class experience : AppCompatActivity() {

    lateinit var et_company_exp : EditText
    lateinit var et_job_exp : EditText
    lateinit var et_sdate : EditText
    lateinit var et_edate : EditText
    lateinit var et_details_exp : EditText
    lateinit var btn_save_exp : Button
    lateinit var ibtn_back4 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.experience)

        et_company_exp = findViewById(R.id.et_company_exp)
        et_job_exp = findViewById(R.id.et_job_exp)
        et_sdate = findViewById(R.id.et_sdate)
        et_edate = findViewById(R.id.et_edate)
        et_details_exp = findViewById(R.id.et_details_exp)
        btn_save_exp = findViewById(R.id.btn_save_exp)
        ibtn_back4 = findViewById(R.id.ibtn_back4)

        btn_save_exp.setOnClickListener {
            if (areFieldsValid()) {
                val company = et_company_exp.text.toString()
                val job = et_job_exp.text.toString()
                val details = et_details_exp.text.toString()
                val startdate = et_sdate.text.toString()
                val enddate = et_edate.text.toString()

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
        ibtn_back4.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun areFieldsValid(): Boolean {
        return when {
            et_company_exp.text.isNullOrEmpty() -> {
                et_company_exp.error = "Field is empty"
                false
            }
            et_job_exp.text.isNullOrEmpty() -> {
                et_job_exp.error = "Field is empty"
                false
            }
            et_details_exp.text.isNullOrEmpty() -> {
                et_details_exp.error = "Field is empty"
                false
            }
            et_sdate.text.isNullOrEmpty() -> {
                et_sdate.error = "Field is empty"
                false
            }
            et_edate.text.isNullOrEmpty() -> {
                et_edate.error = "Field is empty"
                false
            }
            else -> true
        }
    }
}
