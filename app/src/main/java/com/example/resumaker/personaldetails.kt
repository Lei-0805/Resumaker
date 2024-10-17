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

class personaldetails : AppCompatActivity() {

    lateinit var et_name : EditText
    lateinit var et_address : EditText
    lateinit var et_email_perdet : EditText
    lateinit var et_phone_perdet : EditText
    lateinit var et_linkedin : EditText
    lateinit var btn_save_perdet : Button
    lateinit var ibtn_back6 : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.personal_details)

        et_name = findViewById(R.id.et_name)
        et_address = findViewById(R.id.et_address)
        et_email_perdet = findViewById(R.id.et_email_perdet)
        et_phone_perdet = findViewById(R.id.et_phone_perdet)
        et_linkedin = findViewById(R.id.et_linkedin)
        btn_save_perdet = findViewById(R.id.btn_save_perdet)
        ibtn_back6 = findViewById(R.id.ibtn_back6)

        // Save button: Show toast instead of navigating
        btn_save_perdet.setOnClickListener {
            if (areFieldsValid()) {
                val name = et_name.text.toString()
                val address = et_address.text.toString()
                val email = et_email_perdet.text.toString()
                val phone = et_phone_perdet.text.toString()
                val linkedin = et_linkedin.text.toString()

                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()

            }
        }

        ibtn_back6.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_name.text.isNullOrEmpty() -> {
                et_name.error = "Field is empty"
                false
            }
            et_address.text.isNullOrEmpty() -> {
                et_address.error = "Field is empty"
                false
            }
            et_email_perdet.text.isNullOrEmpty() -> {
                et_email_perdet.error = "Field is empty"
                false
            }
            et_phone_perdet.text.isNullOrEmpty() -> {
                et_phone_perdet.error = "Field is empty"
                false
            }
            et_linkedin.text.isNullOrEmpty() -> {
                et_linkedin.error = "Field is empty"
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