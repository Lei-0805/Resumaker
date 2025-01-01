package com.example.resumaker

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDesiredJob: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etLinkedIn: EditText
    private lateinit var btnSave: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var personalDetailsAdapter: PersonalDetailsAdapter
    private lateinit var ibtnBackPersonalDetails: ImageButton

    private val personalDetailsList = mutableListOf<PersonalDetail>()
    private val url = "http://192.168.13.6:8000/api/personal_details_data" // Replace with actual URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_details)

        // Initialize views
        etName = findViewById(R.id.et_name)
        etDesiredJob = findViewById(R.id.et_desired_job)
        etAddress = findViewById(R.id.et_address)
        etEmail = findViewById(R.id.et_email_perdet)
        etPhone = findViewById(R.id.et_phone_perdet)
        etLinkedIn = findViewById(R.id.et_linkedin)
        btnSave = findViewById(R.id.btn_save_perdet)
        ibtnBackPersonalDetails = findViewById(R.id.ibtnBackPersonalDetails)

        // Set up RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView_personal_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        personalDetailsAdapter = PersonalDetailsAdapter(personalDetailsList)
        recyclerView.adapter = personalDetailsAdapter

        // Set validation filters
        setValidationFilters()

        // Load existing data


        // Save button click listener
        btnSave.setOnClickListener {
            if (areFieldsValid()) {
                submitPersonalDetails()
            }
        }

        // Back button
        ibtnBackPersonalDetails.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    // Navigate to a new activity
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    // Validation filters for input
    private fun setValidationFilters() {
        val nameJobFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etName.filters = arrayOf(nameJobFilter)
        etDesiredJob.filters = arrayOf(nameJobFilter)
        etPhone.filters = arrayOf(InputFilter.LengthFilter(15)) // Limit phone to 15 digits
    }

    // Validate input fields
    private fun areFieldsValid(): Boolean {
        return when {
            etName.text.isNullOrEmpty() -> {
                etName.error = "Field is empty"; false
            }
            etDesiredJob.text.isNullOrEmpty() -> {
                etDesiredJob.error = "Field is empty"; false
            }
            etAddress.text.isNullOrEmpty() -> {
                etAddress.error = "Field is empty"; false
            }
            etEmail.text.isNullOrEmpty() -> {
                etEmail.error = "Field is empty"; false
            }
            etPhone.text.isNullOrEmpty() -> {
                etPhone.error = "Field is empty"; false
            }
            etLinkedIn.text.isNullOrEmpty() -> {
                etLinkedIn.error = "Field is empty"; false
            }
            else -> true
        }
    }

    // Submit data to backend
    private fun submitPersonalDetails() {
        val requestQueue = Volley.newRequestQueue(this)
        val personalDetailsObject = JSONObject().apply {
            put("name", etName.text.toString())
            put("desiredJob", etDesiredJob.text.toString())
            put("address", etAddress.text.toString())
            put("email", etEmail.text.toString())
            put("phone", etPhone.text.toString())
            put("linkedIn", etLinkedIn.text.toString())
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, personalDetailsObject,
            { response ->
                Toast.makeText(this, "Personal Details saved successfully", Toast.LENGTH_SHORT).show()
                 // Refresh list after submission
            },
            { error ->
                Toast.makeText(this, "Failed to save personal details data", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
    }

    // Load personal details from backend



}
