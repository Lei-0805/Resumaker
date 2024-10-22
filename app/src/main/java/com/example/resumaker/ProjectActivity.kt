package com.example.resumaker

import Project
import android.annotation.SuppressLint
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

class ProjectActivity : AppCompatActivity() {

    private lateinit var etProjTitle: EditText
    private lateinit var etProjDescription: EditText
    private lateinit var btnSaveProjects: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter

    private var projectList = mutableListOf<Project>() // To hold the project data

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project)

        etProjTitle = findViewById(R.id.et_proj_title)
        etProjDescription = findViewById(R.id.et_proj_description)
        btnSaveProjects = findViewById(R.id.btn_save_projects)
        ibtnBack = findViewById(R.id.ibtn_back7)
        recyclerView = findViewById(R.id.recyclerView_projects)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        projectAdapter = ProjectAdapter(projectList)
        recyclerView.adapter = projectAdapter

        // Load existing project data if needed
        loadProjectData()

        btnSaveProjects.setOnClickListener {
            if (areFieldsValid()) {
                submitProjectData()
            }
        }

        ibtnBack.setOnClickListener {
            finish()  // Close the current activity
        }
    }

    private fun areFieldsValid(): Boolean {
        return when {
            etProjTitle.text.isNullOrEmpty() -> {
                etProjTitle.error = "Field is empty"
                false
            }
            etProjDescription.text.isNullOrEmpty() -> {
                etProjDescription.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    private fun submitProjectData() {
        val projectTitle = etProjTitle.text.toString()
        val projectDescription = etProjDescription.text.toString()

        // Create an instance of the Project data class
        val projectEntry = Project(title = projectTitle, description = projectDescription)

        // Add the new project entry to the project list
        projectList.add(projectEntry)

        // Save updated project data
        saveProjectData()

        // Refresh the RecyclerView to show the new entry
        projectAdapter.notifyDataSetChanged()

        // Feedback to the user
        Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()
    }

    private fun saveProjectData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the projectList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(projectList)

        // Save the JSON string in SharedPreferences
        editor.putString("projectList", json)
        editor.apply()
    }

    private fun loadProjectData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("projectList", null)

        if (json != null) {
            // Deserialize the JSON string back to a List<Project>
            val type = object : TypeToken<MutableList<Project>>() {}.type
            projectList = gson.fromJson(json, type) ?: mutableListOf()
            projectAdapter.notifyDataSetChanged() // Refresh the adapter after loading data
        }
    }

    private fun clearFields() {
        etProjTitle.text.clear()
        etProjDescription.text.clear()
    }
}
