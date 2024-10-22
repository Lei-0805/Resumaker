package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class log_in : AppCompatActivity() {

    lateinit var til_username: TextInputEditText
    lateinit var til_password: TextInputEditText
    lateinit var btn_LogIn: Button
    lateinit var btn_signup1: Button
    private val sharedPrefFile = "UserPrefs"

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
            Request.Method.POST, url,
            { response ->
                try {
                    // Log the raw response for debugging
                    println("Raw response: $response")

                    // Try parsing the JSON response
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")

                    if (message == "Login successful") {
                        // Save login state in SharedPreferences
                        val sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                        navigateTo(navfunction::class.java)
                    } else {
                        Toast.makeText(this, "Login failed: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Log and show the response in case of parsing failure
                    Toast.makeText(this, "Error parsing response: $response", Toast.LENGTH_LONG).show()
                    println("Error parsing response: ${e.message}")
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorData = error.networkResponse.data?.let { String(it) }

                    // Log full error details for debugging
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

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}