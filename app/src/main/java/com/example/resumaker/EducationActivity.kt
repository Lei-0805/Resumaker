package com.example.resumaker

import Education
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

class EducationActivity : AppCompatActivity() {

    private lateinit var etProgram: EditText
    private lateinit var etSchool: EditText
    private lateinit var etSchoolYear: EditText
    private lateinit var btnSaveEduc: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerViewEducation: RecyclerView
    private lateinit var educationAdapter: EducationAdapter

    private var educationList = mutableListOf<Education>() // To hold the education data

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.education)

        etProgram = findViewById(R.id.et_program)
        etSchool = findViewById(R.id.et_school)
        etSchoolYear = findViewById(R.id.et_schoolyear_college)
        btnSaveEduc = findViewById(R.id.btn_save_educ)
        ibtnBack = findViewById(R.id.ibtn_back3)
        recyclerViewEducation = findViewById(R.id.recyclerView_education)

        // Load existing education data
        loadEducationData()

        // Set up RecyclerView
        educationAdapter = EducationAdapter(educationList)
        recyclerViewEducation.layoutManager = LinearLayoutManager(this)
        recyclerViewEducation.adapter = educationAdapter

        btnSaveEduc.setOnClickListener {
            if (areFieldsValid()) {
                submitEducationData()
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

    private fun submitEducationData() {
        val program = etProgram.text.toString()
        val school = etSchool.text.toString()
        val schoolYear = etSchoolYear.text.toString()

        // Create an instance of the Education data class
        val educationEntry = Education(program = program, school = school, schoolYear = schoolYear)

        // Add the new education entry to the education list
        educationList.add(educationEntry)

        // Save updated education data
        saveEducationData()

        // Feedback to the user
        Toast.makeText(this, "Education details saved", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()

        // Notify the adapter of the new data
        educationAdapter.notifyDataSetChanged()
    }

    private fun saveEducationData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the educationList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(educationList)

        // Save the JSON string in SharedPreferences
        editor.putString("educationList", json)
        editor.apply()
    }

    private fun loadEducationData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("educationList", null)

        if (json != null) {
            // Deserialize the JSON string back to a MutableList<Education>
            val type = object : TypeToken<MutableList<Education>>() {}.type
            educationList = gson.fromJson(json, type) ?: mutableListOf()
        }
    }

    private fun clearFields() {
        etProgram.text.clear()
        etSchool.text.clear()
        etSchoolYear.text.clear()
    }
}
