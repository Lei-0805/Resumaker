package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class log_in : ComponentActivity() {

    private lateinit var et_username2: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_LogIn: Button
    private lateinit var btn_signup1: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        et_username2 = findViewById(R.id.et_username2)
        et_password = findViewById(R.id.et_password)
        btn_LogIn = findViewById(R.id.btn_LogIn)
        btn_signup1 = findViewById(R.id.btn_signup1)

        // Handle login button click
        btn_LogIn.setOnClickListener {
            if (areFieldsValid()) {
                val username = et_username2.text.toString()
                val password = et_password.text.toString()

                // Call the login function with the entered credentials
                login(username, password)
            }
        }

        // Handle sign-up button click
        btn_signup1.setOnClickListener {
            val intent = Intent(this, sign_up::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_username2.text.isNullOrEmpty() -> {
                et_username2.error = "Username is required"
                false
            }
            et_password.text.isNullOrEmpty() -> {
                et_password.error = "Password is required"
                false
            }
            else -> true
        }
    }

    // Function to handle login
    private fun login(username: String, password: String) {
        val url = "http://192.168.1.9:8000/api/login"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    println("Response: $response") // Log full response

                    val jsonResponse = JSONObject(response)

                    // Check if the response contains an access token
                    if (jsonResponse.has("access_token")) {
                        val token = jsonResponse.getString("access_token")
                        val user = jsonResponse.getJSONObject("user")
                        val username = user.getString("username")

                        // Save login info in shared preferences
                        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", username)
                        editor.putString("access_token", token)
                        editor.apply()

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to the next activity
                        val intent = Intent(this, navfunction::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle error message if access_token is not present
                        Toast.makeText(this, "Login failed: ${jsonResponse.optString("error", "Unknown error")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["create_password"] = password // Make sure to match the field name
                return params
            }
        }

        // Add the request to the request queue
        queue.add(request)
    }
}