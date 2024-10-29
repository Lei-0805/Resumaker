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

class EducationActivity : AppCompatActivity() {

    private lateinit var etProgram: EditText
    private lateinit var etSchool: EditText
    private lateinit var etSchoolYear: EditText
    private lateinit var btnSaveEduc: Button
    private lateinit var educationAdapter: EducationAdapter
    private lateinit var recyclerView: RecyclerView

    private var educationList = mutableListOf<Education>() // List to hold education data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.education)

        etProgram = findViewById(R.id.et_program)
        etSchool = findViewById(R.id.et_school)
        etSchoolYear = findViewById(R.id.et_schoolyear_college)
        btnSaveEduc = findViewById(R.id.btn_save_educ)
        recyclerView = findViewById(R.id.recyclerView_education)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        educationAdapter = EducationAdapter(educationList)
        recyclerView.adapter = educationAdapter

        // Set validation filters
        setValidationFilters()

        // Load existing education data
        loadEducationData()

        btnSaveEduc.setOnClickListener {
            if (areFieldsValid()) {
                saveEducationData()
                Toast.makeText(this, "Education details saved", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }
    }

    // Set input filters for validation
    private fun setValidationFilters() {
        // Prevent numbers in etProgram and etSchool
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        //Set the filter that do not allow numbers and exceeding input in school year
        etProgram.filters = arrayOf(noNumbersFilter)
        etSchool.filters = arrayOf(noNumbersFilter)
        etSchoolYear.filters = arrayOf(InputFilter.LengthFilter(9))
    }

    private fun areFieldsValid(): Boolean {
        return when {
            etProgram.text.isNullOrEmpty() -> {
                etProgram.error = "Field is empty"
                false
            }
            etSchool.text.isNullOrEmpty() -> {
                etSchool.error = "Field is empty"
                false
            }
            etSchoolYear.text.isNullOrEmpty() -> {
                etSchoolYear.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    private fun saveEducationData() {
        val program = etProgram.text.toString()
        val school = etSchool.text.toString()
        val schoolYear = etSchoolYear.text.toString()

        // Create an instance of the Education data class
        val educationEntry = Education(program = program, school = school, schoolYear = schoolYear)

        // Add the new education entry to the list
        educationList.add(educationEntry)

        // Save updated ResumeData
        saveResumeData()

        // Refresh the RecyclerView to show the new entry
        educationAdapter.notifyDataSetChanged()
    }

    private fun loadEducationData() {
        // Load current ResumeData
        val resumeData = loadResumeData()

        // Add existing education entries to the list
        educationList.clear() // Clear the existing list before adding new items
        educationList.addAll(resumeData.education)
        educationAdapter.notifyDataSetChanged() // Refresh the RecyclerView
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

    private fun saveResumeData() {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val resumeData = loadResumeData() // Load current ResumeData

        // Update the ResumeData with the current educationList
        resumeData.education = educationList

        // Save updated ResumeData
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()
    }

    private fun clearFields() {
        etProgram.text.clear()
        etSchool.text.clear()
        etSchoolYear.text.clear()
    }
}
