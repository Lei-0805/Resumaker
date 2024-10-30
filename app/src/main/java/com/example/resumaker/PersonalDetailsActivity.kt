package com.example.resumaker

import android.annotation.SuppressLint
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
import com.google.gson.Gson

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDesiredJob: EditText
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etLinkedIn: EditText
    private lateinit var ibtnBackPersonalDetails: ImageButton
    private lateinit var btnSave: Button
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
        ibtnBackPersonalDetails = findViewById(R.id.ibtnBackPersonalDetails)
        btnSave = findViewById(R.id.btn_save_perdet)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView_personal_details)
        recyclerView.layoutManager = LinearLayoutManager(this)
        personalDetailsAdapter = PersonalDetailsAdapter(personalDetailsList)
        recyclerView.adapter = personalDetailsAdapter

        // Load existing personal details if the personal details is filled up and saved
        loadPersonalDetails()

        // Set validation filters
        setValidationFilters()

        // Save button click listener
        btnSave.setOnClickListener {
            if (areFieldsValid()) {
                submitData()
            }
        }
        ibtnBackPersonalDetails.setOnClickListener{
            navigateTo(createpage::class.java)
        }
    }

    // Function to navigate to the next page
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    // Set filters and input validations
    private fun setValidationFilters() {
        // Prevent numbers in etName and etDesiredJob
        val nameJobFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etName.filters = arrayOf(nameJobFilter)
        etDesiredJob.filters = arrayOf(nameJobFilter)

        // Limit phone number to 15 digits
        etPhone.filters = arrayOf(InputFilter.LengthFilter(15))
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

        // Save personal details in ResumeData
        val resumeData = ResumeData(personalDetails = personalDetailsList)
        saveResumeData(resumeData)

        Toast.makeText(this, "Personal Details saved successfully", Toast.LENGTH_SHORT).show()

        // Clear input fields after saving
        clearInputFields()
    }

    // Save ResumeData to SharedPreferences
    private fun saveResumeData(resumeData: ResumeData) {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(resumeData)
        sharedPreferences.putString("resumeData", json)
        sharedPreferences.apply()
    }

    // Load existing personal details from SharedPreferences
    @SuppressLint("NotifyDataSetChanged")
    private fun loadPersonalDetails() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)
        if (json != null) {
            val resumeData = gson.fromJson(json, ResumeData::class.java)
            personalDetailsList.clear()
            personalDetailsList.addAll(resumeData.personalDetails)
            personalDetailsAdapter.notifyDataSetChanged()
        }
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
