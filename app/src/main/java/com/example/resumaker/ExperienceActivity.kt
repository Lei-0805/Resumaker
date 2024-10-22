package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExperienceActivity : AppCompatActivity() {

    private lateinit var etCompanyExp: EditText
    private lateinit var etJobExp: EditText
    private lateinit var etSdate: EditText
    private lateinit var etEdate: EditText
    private lateinit var etDetailsExp: EditText
    private lateinit var btnSaveExp: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var experienceAdapter: ExperienceAdapter

    private var experienceList = mutableListOf<Map<String, String>>() // Holds the experience data as a list of maps

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.experience)

        // Initialize views
        etCompanyExp = findViewById(R.id.et_company_exp)
        etJobExp = findViewById(R.id.et_job_exp)
        etSdate = findViewById(R.id.et_sdate)
        etEdate = findViewById(R.id.et_edate)
        etDetailsExp = findViewById(R.id.et_details_exp)
        btnSaveExp = findViewById(R.id.btn_save_exp)
        ibtnBack = findViewById(R.id.ibtn_back4)
        recyclerView = findViewById(R.id.recyclerView_experience)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        experienceAdapter = ExperienceAdapter(experienceList)
        recyclerView.adapter = experienceAdapter

        // Load existing experience data
        loadExperienceData()

        btnSaveExp.setOnClickListener {
            if (areFieldsValid()) {
                submitExperienceData()
            }
        }

        ibtnBack.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            etCompanyExp.text.isNullOrEmpty() -> {
                etCompanyExp.error = "Field is empty"
                false
            }
            etJobExp.text.isNullOrEmpty() -> {
                etJobExp.error = "Field is empty"
                false
            }
            etDetailsExp.text.isNullOrEmpty() -> {
                etDetailsExp.error = "Field is empty"
                false
            }
            etSdate.text.isNullOrEmpty() -> {
                etSdate.error = "Field is empty"
                false
            }
            etEdate.text.isNullOrEmpty() -> {
                etEdate.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    private fun submitExperienceData() {
        val company = etCompanyExp.text.toString()
        val job = etJobExp.text.toString()
        val details = etDetailsExp.text.toString()
        val startdate = etSdate.text.toString()
        val enddate = etEdate.text.toString()

        // Create a map for the experience entry
        val experienceEntry = mapOf(
            "company" to company,
            "jobTitle" to job,
            "startDate" to startdate,
            "endDate" to enddate,
            "details" to details
        )

        // Add the new experience entry to the experience list
        experienceList.add(experienceEntry)

        // Save updated experience data
        saveExperienceData()

        // Refresh the RecyclerView to show the new entry
        experienceAdapter.notifyDataSetChanged()

        // Feedback to the user
        Toast.makeText(this, "Experience details saved", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()
    }

    private fun saveExperienceData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the experienceList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(experienceList)

        // Save the JSON string in SharedPreferences
        editor.putString("experienceList", json)
        editor.apply()
    }

    private fun loadExperienceData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("experienceList", null)

        if (json != null) {
            // Deserialize the JSON string back to a List<Map<String, String>>
            val type = object : TypeToken<MutableList<Map<String, String>>>() {}.type
            experienceList = gson.fromJson(json, type) ?: mutableListOf()
            experienceAdapter.notifyDataSetChanged() // Refresh the adapter after loading data
        }
    }

    private fun clearFields() {
        etCompanyExp.text.clear()
        etJobExp.text.clear()
        etSdate.text.clear()
        etEdate.text.clear()
        etDetailsExp.text.clear()
    }
}
