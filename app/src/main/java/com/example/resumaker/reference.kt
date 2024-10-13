package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class reference : AppCompatActivity() {

    lateinit var et_ref_name : EditText
    lateinit var et_job_ref : EditText
    lateinit var et_company_ref : EditText
    lateinit var et_email_ref : EditText
    lateinit var et_phone_ref : EditText
    lateinit var btn_save_ref : Button
    lateinit var ibtn_back8 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.reference)

        et_ref_name = findViewById(R.id.et_ref_name)
        et_job_ref = findViewById(R.id.et_job_ref)
        et_company_ref = findViewById(R.id.et_company_ref)
        et_email_ref = findViewById(R.id.et_email_ref)
        et_phone_ref = findViewById(R.id.et_phone_ref)
        btn_save_ref = findViewById(R.id.btn_save_ref)
        ibtn_back8 = findViewById(R.id.ibtn_back8)

        btn_save_ref.setOnClickListener {
            if (areFieldsValid()) {
                val ref_name = et_ref_name.text.toString()
                val ref_job = et_job_ref.text.toString()
                val ref_email = et_email_ref.text.toString()
                val ref_phone = et_phone_ref.text.toString()
                val ref_company = et_company_ref.toString()
            }
        }
        ibtn_back8.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_ref_name.text.isNullOrEmpty() -> {
                et_ref_name.error = "Field is empty"
                false
            }
            et_job_ref.text.isNullOrEmpty() -> {
                et_job_ref.error = "Field is empty"
                false
            }
            et_email_ref.text.isNullOrEmpty() -> {
                et_email_ref.error = "Field is empty"
                false
            }
            et_phone_ref.text.isNullOrEmpty() -> {
                et_phone_ref.error = "Field is empty"
                false
            }
            et_company_ref.text.isNullOrEmpty() -> {
                et_company_ref.error = "Field is empty"
                false
            }
            else -> true
        }
    }
}
private fun Button.setOnClickListener(activity: AppCompatActivity, nextPage: Class<*>) {
    this.setOnClickListener {
        val intent = Intent(activity, nextPage)
        activity.startActivity(intent)
    }
}
