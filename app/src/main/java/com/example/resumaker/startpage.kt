package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class startpage : AppCompatActivity() {

    lateinit var btn_get_started : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.startpage)

        btn_get_started = findViewById(R.id.btn_get_started)

        btn_get_started.setOnClickListener(this, MainActivity::class.java)
    }
}

private fun Button.setOnClickListener(activity: AppCompatActivity, nextPage: Class<*>) {
    this.setOnClickListener {
        val intent = Intent(activity, nextPage)
        activity.startActivity(intent)
    }
}