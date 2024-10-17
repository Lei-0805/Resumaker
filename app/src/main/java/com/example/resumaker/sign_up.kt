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

class sign_up : AppCompatActivity() {

    lateinit var til_create_username: TextInputEditText
    lateinit var til_create_password: TextInputEditText
    lateinit var til_confirm_password: TextInputEditText
    lateinit var btnSignUp: Button
    lateinit var btnLogIn1: Button
    private val sharedPrefFile = "UserPrefs"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        til_create_username = findViewById(R.id.til_create_username)
        til_create_password = findViewById(R.id.til_create_password)
        til_confirm_password = findViewById(R.id.til_confirm_password)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogIn1 = findViewById(R.id.btnLogIn1)

        btnSignUp.setOnClickListener {
            val username = til_create_username.text.toString()
            val createPassword = til_create_password.text.toString()
            val confirmPassword = til_confirm_password.text.toString()

            if (createPassword == confirmPassword) {
                signup(username, createPassword, confirmPassword)
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogIn1.setOnClickListener {
            navigateTo(log_in::class.java)
        }
    }

    private fun signup(username: String, create_password: String, confirm_password: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.9:8000/api/signup_users"

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    // Try parsing JSON response
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.getString("message")

                    if (message == "Sign up successful") {
                        // Save login state in SharedPreferences after successful signup
                        val sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        navigateTo(navfunction::class.java)
                    } else {
                        Toast.makeText(this, "Signup failed: $message", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Log the response for debugging
                    Toast.makeText(this, "Error parsing response: ${response}", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorData = error.networkResponse.data?.let { String(it) }

                    // Log full error response for debugging
                    Toast.makeText(this, "Error: $statusCode - $errorData", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["create_password"] = create_password
                params["confirm_password"] = confirm_password
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
