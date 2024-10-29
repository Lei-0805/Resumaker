package com.example.resumaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RateFragment : Fragment() {

    private lateinit var ratingbar: RatingBar
    private lateinit var ratingscale: TextView
    private lateinit var et_feedback: EditText
    private lateinit var btn_rate_submit: Button

    private val BASE_URL = "http://192.168.1.9:8000/"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rate, container, false)

        // Initialize views
        ratingbar = view.findViewById(R.id.ratingbar)
        ratingscale = view.findViewById(R.id.ratingscale)
        et_feedback = view.findViewById(R.id.et_feedback)
        btn_rate_submit = view.findViewById(R.id.btn_rate_submit)

        ratingbar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingscale.text = rating.toString()
            ratingscale.text = when (rating.toInt()) {
                1 -> "Very bad"
                2 -> "Need some improvement"
                3 -> "Good"
                4 -> "Great"
                5 -> "Excellent"
                else -> ""
            }
        }

        // Set click listener for submit button
        btn_rate_submit.setOnClickListener {
            if (areFieldsValid()) {
                val ratingValue = ratingbar.rating.toInt() // Get the selected rating
                val feedback = et_feedback.text.toString()
                submitRating(ratingValue, feedback)
                et_feedback.text.clear()
                ratingbar.rating = 0f
            } else {
                showToast("Please fill in the feedback text box")
            }
        }

        // Fetch ratings on view creation
        fetchRatings()

        return view
    }

    private fun areFieldsValid(): Boolean {
        return if (et_feedback.text.isNullOrEmpty()) {
            et_feedback.error = "Field is Empty"
            false
        } else {
            true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchRatings() {
        val url = "${BASE_URL}api/ratings_data"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                handleFetchResponse(response)
            },
            { error: VolleyError ->
                showToast("Error: ${error.message}")
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun handleFetchResponse(response: JSONArray) {
        // Handle the response, e.g., convert it to a list of ratings
        for (i in 0 until response.length()) {
            val ratingObject: JSONObject = response.getJSONObject(i)
            val rating = ratingObject.getInt("rating")
            val feedback = ratingObject.getString("feedback")
            println("Rating: $rating, Feedback: $feedback")
        }
    }

    private fun submitRating(rating: Int, feedback: String) {
        val url = "${BASE_URL}api/ratings_data" // Change to your actual endpoint
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObject = JSONObject().apply {
            put("rating", rating)
            put("feedback", feedback)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener { response ->
                showToast("Rating submitted successfully")
            },
            Response.ErrorListener { error: VolleyError ->
                showToast("Failed to submit rating: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}
