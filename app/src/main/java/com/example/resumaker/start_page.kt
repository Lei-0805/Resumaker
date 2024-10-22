package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class start_page : AppCompatActivity() {

    lateinit var btn_get_started: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if the user is already logged in
        if (isUserLoggedIn()) {
            navigateTo(navfunction::class.java)
            return // Exit after navigating
        } else if (isUserSignedIn(this)) { // Check if the user is signed in
            navigateTo(navfunction::class.java)
            return // Exit after navigating
        }

        // If neither, show the start page
        setContentView(R.layout.startpage)

        btn_get_started = findViewById(R.id.btn_get_started)

        btn_get_started.setOnClickListener {
            navigateTo(sign_up::class.java)
        }
    }

    // Function to check if the user is logged in
    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", true) // Default is false if not set
    }

    // Function to check if the user is signed in
    private fun isUserSignedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_signed_in", true) // Default is false if not signed in
    }

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
