package com.example.resumaker

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ExperienceActivity : AppCompatActivity() {

    private lateinit var etCompanyExp: EditText
    private lateinit var etJobExp: EditText
    private lateinit var etSdate: EditText
    private lateinit var etEdate: EditText
    private lateinit var etDetailsExp: EditText
    private lateinit var btnSaveExp: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var experienceAdapter: ExperienceAdapter
    private var experienceList: MutableList<Experience> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.experience)

        // Initialize views
        etCompanyExp = findViewById(R.id.et_company_exp)
        etJobExp = findViewById(R.id.et_job_exp)
        etSdate = findViewById(R.id.et_sdate)
        etEdate = findViewById(R.id.et_edate)
        etDetailsExp = findViewById(R.id.et_details_exp)
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
                Toast.makeText(this, "Experience details saved", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }
    }

    private fun setValidationFilters() {
        // Prevent numbers in etDetailsExp, etCompanyExp, and etJobTitle
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        //Set the filter that do not allow numbers and exceeding input in sdate & edate
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

    private fun saveExperienceData() {
        val company = etCompanyExp.text.toString()
        val job = etJobExp.text.toString()
        val details = etDetailsExp.text.toString()
        val startdate = etSdate.text.toString()
        val enddate = etEdate.text.toString()

        // Create an instance of the Experience data class
        val experienceEntry = Experience(company, job, startdate, enddate, details)

        // Add to the local list and notify adapter
        experienceList.add(experienceEntry)
        experienceAdapter.notifyDataSetChanged()

        // Load current ResumeData
        val resumeData = loadResumeData()
        resumeData.experience = experienceList

        // Save updated ResumeData
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()
    }

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
            ResumeData()  // Provide default data if nothing is found
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
