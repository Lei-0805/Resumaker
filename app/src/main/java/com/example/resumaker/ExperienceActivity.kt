package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.gson.Gson
import org.json.JSONObject

class ExperienceActivity : AppCompatActivity() {

    private lateinit var etCompanyExp: EditText
    private lateinit var etJobExp: EditText
    private lateinit var etSdate: EditText
    private lateinit var etEdate: EditText
    private lateinit var etDetailsExp: EditText
    private lateinit var ibtnBackExperience: ImageButton
    private lateinit var btnSaveExp: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var experienceAdapter: ExperienceAdapter
    private var experienceList: MutableList<Experience> = mutableListOf()
    private val submitUrl = "http://192.168.13.6:8000/api/experience_data" // Replace with your actual URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.experience)

        // Initialize views
        etCompanyExp = findViewById(R.id.et_company_exp)
        etJobExp = findViewById(R.id.et_job_exp)
        etSdate = findViewById(R.id.et_sdate)
        etEdate = findViewById(R.id.et_edate)
        etDetailsExp = findViewById(R.id.et_details_exp)
        ibtnBackExperience = findViewById(R.id.ibtnBackExperience)
        btnSaveExp = findViewById(R.id.btn_save_exp)
        recyclerView = findViewById(R.id.recyclerView_experience)

        // Set up RecyclerView with the ExperienceAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        experienceAdapter = ExperienceAdapter(experienceList)
        recyclerView.adapter = experienceAdapter

        // Load existing experiences into the RecyclerView
        loadExperienceData()

        // Set validation filters
        setValidationFilters()

        // Button click listener
        btnSaveExp.setOnClickListener {
            if (areFieldsValid()) {
                saveExperienceData()
                submitExperienceToDatabase() // Send experience to backend
                Toast.makeText(this, "Experience saved successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }
        ibtnBackExperience.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    private fun submitExperienceToDatabase() {
        val company = etCompanyExp.text.toString()
        val job = etJobExp.text.toString()
        val startdate = etSdate.text.toString()
        val enddate = etEdate.text.toString()
        val details = etDetailsExp.text.toString()

        val requestQueue = Volley.newRequestQueue(this)
        val experienceObject = JSONObject().apply {
            put("company", company)
            put("job", job)
            put("start_date", startdate)
            put("end_date", enddate)
            put("details", details)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, submitUrl, experienceObject,
            { response ->
                Toast.makeText(this, "Experience saved successfully", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Failed to save experience data", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    // Function to navigate to the next page
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etDetailsExp.filters = arrayOf(noNumbersFilter)
        etCompanyExp.filters = arrayOf(noNumbersFilter)
        etJobExp.filters = arrayOf(noNumbersFilter)
        etSdate.filters = arrayOf(InputFilter.LengthFilter(4))
        etEdate.filters = arrayOf(InputFilter.LengthFilter(4))
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

    @SuppressLint("NotifyDataSetChanged")
    private fun saveExperienceData() {
        val experienceEntry = Experience(
            etCompanyExp.text.toString(),
            etJobExp.text.toString(),
            etSdate.text.toString(),
            etEdate.text.toString(),
            etDetailsExp.text.toString()
        )
        experienceList.add(experienceEntry)
        experienceAdapter.notifyDataSetChanged()

        val resumeData = loadResumeData()
        resumeData.experience = experienceList

        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadExperienceData() {
        val resumeData = loadResumeData()
        experienceList.clear()
        experienceList.addAll(resumeData.experience)
        experienceAdapter.notifyDataSetChanged()
    }

    private fun loadResumeData(): ResumeData {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)
        return if (json != null) {
            gson.fromJson(json, ResumeData::class.java)
        } else {
            ResumeData()
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
