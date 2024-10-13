

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

class RateFragment : Fragment() {

    private lateinit var ratingbar: RatingBar
    private lateinit var ratingscale: TextView
    private lateinit var et_feedback: EditText
    private lateinit var btn_rate_submit: Button

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

        ratingbar.setOnRatingBarChangeListener { ratingBar, v, b ->
            ratingscale.text = v.toString()
            when (ratingBar.rating.toInt()) {
                1 -> ratingscale.text = "Very bad"
                2 -> ratingscale.text = "Need some improvement"
                3 -> ratingscale.text = "Good"
                4 -> ratingscale.text = "Great"
                5 -> ratingscale.text = "Excellent"
                else -> ratingscale.text = ""
            }
        }

        // Set click listener for submit button
        btn_rate_submit.setOnClickListener {
            if (areFieldsValid()) {
                val feedback = et_feedback.text.toString()
                // You might want to do something with the feedback variable here
                et_feedback.text.clear()
                ratingbar.rating = 0f
                showToast("Thank you for sharing your feedback")
            } else {
                showToast("Please fill in the feedback text box")
            }
        }

        return view
    }

    private fun areFieldsValid(): Boolean {
        return when {
            et_feedback.text.isNullOrEmpty() -> {
                et_feedback.error = "Field is Empty"
                false
            }
            else -> true
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}

        
        