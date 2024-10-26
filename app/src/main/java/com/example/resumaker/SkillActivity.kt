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

class SkillActivity : AppCompatActivity() {

    private lateinit var etSkill: EditText
    private lateinit var btnSaveSkill: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var skillAdapter: SkillAdapter

    private var skillList = mutableListOf<Skill>() // To hold the skill data

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // For visual consistency
        setContentView(R.layout.skill)

        // Initialize UI components
        etSkill = findViewById(R.id.et_skill)
        btnSaveSkill = findViewById(R.id.btn_save_skill)
        ibtnBack = findViewById(R.id.ibtn_back9)
        recyclerView = findViewById(R.id.recyclerView_skill)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        skillAdapter = SkillAdapter(skillList)
        recyclerView.adapter = skillAdapter

        // Load existing skill data
        loadSkillData()

        // Save skill button click listener
        btnSaveSkill.setOnClickListener {
            if (areFieldsValid()) {
                submitSkillData()
            }
        }

        // Back button click listener
        ibtnBack.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Validate input fields
    private fun areFieldsValid(): Boolean {
        return if (etSkill.text.isNullOrEmpty()) {
            etSkill.error = "Field is empty"
            false
        } else {
            true
        }
    }

    // Submit skill data
    private fun submitSkillData() {
        val skillName = etSkill.text.toString()

        // Create an instance of the Skill data class
        val skillEntry = Skill(skillName = skillName)

        // Load current ResumeData
        val resumeData = loadResumeData()

        // Add the new skill entry to the resumeData
        resumeData.skills.add(skillEntry)

        // Save updated ResumeData
        saveResumeData(resumeData)

        // Show feedback to the user
        Toast.makeText(this, "Skill details saved", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()

        // Update the local skill list and notify the adapter
        skillList.add(skillEntry)
        skillAdapter.notifyItemInserted(skillList.size - 1)
    }

    // Load ResumeData from SharedPreferences
    private fun loadResumeData(): ResumeData {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)
        return if (json != null) {
            gson.fromJson(json, ResumeData::class.java)
        } else {
            ResumeData()  // Provide default data if nothing is found
        }
    }

    // Save updated ResumeData to SharedPreferences
    private fun saveResumeData(resumeData: ResumeData) {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()
    }

    // Load skill data from SharedPreferences (if needed for the adapter)
    private fun loadSkillData() {
        val resumeData = loadResumeData()
        skillList.clear() // Clear the existing list
        skillList.addAll(resumeData.skills) // Add the skills from ResumeData
        skillAdapter.notifyDataSetChanged() // Notify the adapter about data changes
    }

    // Clear input fields
    private fun clearFields() {
        etSkill.text.clear()
    }
}
