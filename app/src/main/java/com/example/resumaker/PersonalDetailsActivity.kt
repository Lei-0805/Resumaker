package com.example.resumaker

import PersonalDetail
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDesiredJob: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etLinkedIn: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var personalDetailsAdapter: PersonalDetailsAdapter

    // List to hold personal details
    private val personalDetailsList = mutableListOf<PersonalDetail>()

    @SuppressLint("MissingInflatedId")
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
        btnBack = findViewById(R.id.ibtn_back6)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView_personal_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        personalDetailsAdapter = PersonalDetailsAdapter(personalDetailsList)
        recyclerView.adapter = personalDetailsAdapter

        // Save button click listener
        btnSave.setOnClickListener {
            if (areFieldsValid()) {
                submitData()
            }
        }

        // Back button click listener
        btnBack.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Validate input fields
    private fun areFieldsValid(): Boolean {
        return when {
            etName.text.isNullOrEmpty() -> {
                etName.error = "Field is empty"
                false
            }
            etDesiredJob.text.isNullOrEmpty() -> {
                etDesiredJob.error = "Field is empty"
                false
            }
            etAddress.text.isNullOrEmpty() -> {
                etAddress.error = "Field is empty"
                false
            }
            etEmail.text.isNullOrEmpty() -> {
                etEmail.error = "Field is empty"
                false
            }
            etPhone.text.isNullOrEmpty() -> {
                etPhone.error = "Field is empty"
                false
            }
            etLinkedIn.text.isNullOrEmpty() -> {
                etLinkedIn.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    // Submit data and save to SharedPreferences
    private fun submitData() {
        val name = etName.text.toString()
        val desiredJob = etDesiredJob.text.toString()
        val address = etAddress.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val linkedIn = etLinkedIn.text.toString()

        // Create and add a new personal detail to the list
        val personalDetail = PersonalDetail(name, desiredJob, address, email, phone, linkedIn)
        personalDetailsList.clear() // Assuming only one set of details is needed
        personalDetailsList.add(personalDetail)

        // Save personal details in SharedPreferences
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE).edit()
        sharedPreferences.putString("name", name)
        sharedPreferences.putString("desiredJob", desiredJob)
        sharedPreferences.putString("address", address)
        sharedPreferences.putString("email", email)
        sharedPreferences.putString("phone", phone)
        sharedPreferences.putString("linkedIn", linkedIn)
        sharedPreferences.apply()

        Toast.makeText(this, "Personal Details saved successfully", Toast.LENGTH_SHORT).show()

        // Clear input fields after saving
        clearInputFields()
    }

    // Clear input fields
    private fun clearInputFields() {
        etName.text.clear()
        etDesiredJob.text.clear()
        etAddress.text.clear()
        etEmail.text.clear()
        etPhone.text.clear()
        etLinkedIn.text.clear()
    }
}
