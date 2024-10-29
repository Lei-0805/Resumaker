package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class log_in : AppCompatActivity() {

    lateinit var til_username: TextInputEditText
    lateinit var til_password: TextInputEditText
    lateinit var btn_LogIn: Button
    lateinit var btn_signup1: Button
    private val sharedPrefFile = "UserPrefs" // Ensure this matches the one in sign_up

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        til_username = findViewById(R.id.til_username)
        til_password = findViewById(R.id.til_password)
        btn_LogIn = findViewById(R.id.btn_LogIn)
        btn_signup1 = findViewById(R.id.btn_signup1)

        btn_LogIn.setOnClickListener {
            val username = til_username.text.toString()
            val password = til_password.text.toString()

            login(username, password)
        }
        btn_signup1.setOnClickListener {
            navigateTo(sign_up::class.java)
        }
    }

    private fun login(username: String, create_password: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.9:8000/api/login_users"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    println("Raw response: $response")

                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")

                    if (message == "Login successful") {
                        // Save setup complete flag in SharedPreferences
                        setSetupComplete()

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        navigateTo(navfunction::class.java)
                    } else {
                        Toast.makeText(this, "Login failed: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response: $response", Toast.LENGTH_LONG).show()
                    println("Error parsing response: ${e.message}")
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorData = error.networkResponse.data?.let { String(it) }
                    Toast.makeText(this, "Error: $statusCode - $errorData", Toast.LENGTH_LONG).show()
                    println("Error: $statusCode - $errorData")
                } else {
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_LONG).show()
                    println("Network error: ${error.message}")
                }
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["create_password"] = create_password
                return params
            }
        }

        queue.add(request)
    }

    // Function to set setup completion in SharedPreferences
    private fun setSetupComplete() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_setup_complete", true).apply() // Set the flag
    }

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
