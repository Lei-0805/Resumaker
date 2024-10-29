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
    private val sharedPrefFile = "UserPrefs"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if the user has already completed the first-time setup or sign-in process
        if (isUserSetupComplete()) {
            navigateTo(navfunction::class.java)
            return // Exit after navigating
        }

        // If the user has not completed setup, show the start page
        setContentView(R.layout.startpage)

        btn_get_started = findViewById(R.id.btn_get_started)

        btn_get_started.setOnClickListener {
            navigateTo(sign_up::class.java)
        }
    }

    // Function to check if the user setup is complete
    private fun isUserSetupComplete(): Boolean {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_setup_complete", false) // Default is false if not set
    }

    // Function to navigate to the next page
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
