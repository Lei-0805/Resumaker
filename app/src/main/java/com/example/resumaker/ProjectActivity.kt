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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ProjectActivity : AppCompatActivity() {

    private lateinit var etProjTitle: EditText
    private lateinit var etProjDescription: EditText
    private lateinit var ibtnBackProject: ImageButton
    private lateinit var btnSaveProjects: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter

    private var projectList = mutableListOf<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project)

        etProjTitle = findViewById(R.id.et_proj_title)
        etProjDescription = findViewById(R.id.et_proj_description)
        ibtnBackProject = findViewById(R.id.ibtnBackProject)
        btnSaveProjects = findViewById(R.id.btn_save_projects)
        recyclerView = findViewById(R.id.recyclerView_projects)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        projectAdapter = ProjectAdapter(projectList)
        recyclerView.adapter = projectAdapter

        // Load saved projects and refresh the RecyclerView
        loadSavedProjects()

        // Set validation filters
        setValidationFilters()

        btnSaveProjects.setOnClickListener {
            if (areFieldsValid()) {
                submitProjectData()
            }
        }
        ibtnBackProject.setOnClickListener{
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

    private fun setValidationFilters() {
        //Prevent the etProjectTitle and etProjectDescription to input numbers
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        //Set the filter that do not allow numbers
        etProjTitle.filters = arrayOf(noNumbersFilter)
        etProjDescription.filters = arrayOf(noNumbersFilter)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun submitProjectData() {
        val projectTitle = etProjTitle.text.toString()
        val projectDescription = etProjDescription.text.toString()
        val projectEntry = Project(projectTitle = projectTitle, projectDescription = projectDescription)

        val resumeData = loadResumeData()
        resumeData.projects.add(projectEntry)

        saveResumeData(resumeData)

        projectList.add(projectEntry)
        projectAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun saveResumeData(resumeData: ResumeData) {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun loadSavedProjects() {
        val resumeData = loadResumeData()
        projectList.clear()
        projectList.addAll(resumeData.projects)
        projectAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        etProjTitle.text.clear()
        etProjDescription.text.clear()
    }
}
