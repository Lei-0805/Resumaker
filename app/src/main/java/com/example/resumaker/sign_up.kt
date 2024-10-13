package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class sign_up : AppCompatActivity() {

    lateinit var et_username1: EditText
    lateinit var et_create_password: EditText
    lateinit var et_confirm_password: EditText
    lateinit var btnSignUp: Button
    lateinit var btnLogIn1: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        et_username1 = findViewById(R.id.et_username1)
        et_create_password = findViewById(R.id.et_create_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogIn1 = findViewById(R.id.btnLogIn1)

        btnSignUp.setOnClickListener {
            if (areFieldsValid()) {
                val username = et_username1.text.toString()
                val createPassword = et_create_password.text.toString()
                val confirmPassword = et_confirm_password.text.toString()

                signupWithHttpUrlConnection(username, createPassword, confirmPassword)
            }
        }

        btnLogIn1.setOnClickListener {
            val intent = Intent(this, log_in::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_username1.text.isNullOrEmpty() -> {
                et_username1.error = "Username is required"
                false
            }

            et_username1.text.length < 7 -> {
                et_username1.error = "Username must be at least 7 characters"
                false
            }

            et_username1.text.length > 10 -> {
                et_username1.error = "Username cannot be more than 10 characters"
                false
            }

            et_create_password.text.isNullOrEmpty() -> {
                et_create_password.error = "Password is required"
                false
            }

            et_create_password.text.length < 6 -> {
                et_create_password.error = "Password must be at least 6 characters"
                false
            }

            et_create_password.text.length > 8 -> {
                et_create_password.error = "Password cannot be more than 8 characters"
                false
            }

            et_confirm_password.text.isNullOrEmpty() -> {
                et_confirm_password.error = "Confirm password is required"
                false
            }

            et_create_password.text.toString() != et_confirm_password.text.toString() -> {
                et_confirm_password.error = "Passwords do not match"
                false
            }

            else -> true
        }
    }

    private fun signupWithHttpUrlConnection(
        username: String,
        createPassword: String,
        confirmPassword: String
    ) {
        // Run the network request on a background thread
        Thread {
            try {
                val url = URL("http://192.168.1.9:8000/api/signup_users")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                urlConnection.setRequestProperty("Content-Type", "application/json")  // Set to JSON

                // Create JSON object with the parameters
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username)
                jsonRequest.put("create_password", createPassword)
                jsonRequest.put("confirm_password", confirmPassword)

                // Log the JSON request for debugging
                Log.d("JSON Payload", jsonRequest.toString())

                // Write the JSON data to the output stream
                val outputStream: OutputStream = urlConnection.outputStream
                outputStream.write(jsonRequest.toString().toByteArray())
                outputStream.flush()
                outputStream.close()

                // Check the response code and handle accordingly
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                Log.d("HTTP Response", "Code: $responseCode, Message: $responseMessage")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Success: Process the response and navigate to the next activity
                    runOnUiThread {
                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, navfunction::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Failure: Read the error response
                    val errorStream = urlConnection.errorStream?.bufferedReader()?.readText()
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Error: $responseCode - $errorStream",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("HTTP Error", "Error: $responseCode, Message: $errorStream")
                    }
                }

                // Disconnect after processing
                urlConnection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Exception", e.message.toString())
                }
            }
        }.start() // Start the background thread
    }
}
