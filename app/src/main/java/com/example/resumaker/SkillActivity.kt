package com.example.resumaker

import Skill
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

        // Add the new skill entry to the skill list
        skillList.add(skillEntry)

        // Save updated skill data
        saveSkillData()

        // Show feedback to the user
        Toast.makeText(this, "Skill details saved", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()

        // Notify the adapter of the new data (insert at last position)
        skillAdapter.notifyItemInserted(skillList.size - 1)
    }

    // Save skill data to SharedPreferences
    private fun saveSkillData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the skillList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(skillList)

        // Save the JSON string in SharedPreferences
        editor.putString("skillList", json)
        editor.apply()
    }

    // Load skill data from SharedPreferences
    private fun loadSkillData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("skillList", null)

        if (json != null) {
            // Deserialize the JSON string back to a MutableList<Skill>
            val type = object : TypeToken<MutableList<Skill>>() {}.type
            skillList = gson.fromJson(json, type) ?: mutableListOf()
            skillAdapter.notifyDataSetChanged()
        }
    }

    // Clear input fields
    private fun clearFields() {
        etSkill.text.clear()
    }
}
