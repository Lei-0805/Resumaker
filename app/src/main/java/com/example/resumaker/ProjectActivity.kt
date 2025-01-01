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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ProjectActivity : AppCompatActivity() {

    private lateinit var etProjTitle: EditText
    private lateinit var etProjDescription: EditText
    private lateinit var ibtnBackProject: ImageButton
    private lateinit var btnSaveProjects: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var requestQueue: RequestQueue

    private var projectList = mutableListOf<Project>()
    private val serverUrl = "http://192.168.13.6:8000/api/project_data" // Replace with actual URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Set validation filters
        setValidationFilters()

        // Load existing project data from backend
        loadProjectDataFromDatabase()

        btnSaveProjects.setOnClickListener {
            if (areFieldsValid()) {
                saveProjectDataToDatabase()
                clearFields()
            }
        }

        ibtnBackProject.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
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
    private fun saveProjectDataToDatabase() {
        val projectTitle = etProjTitle.text.toString()
        val projectDescription = etProjDescription.text.toString()

        // JSON payload
        val params = JSONObject()
        params.put("projectTitle", projectTitle)
        params.put("projectDescription", projectDescription)

        // Volley POST request
        val request = JsonObjectRequest(
            Request.Method.POST, serverUrl, params,
            { response ->
                Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
                loadProjectDataFromDatabase() // Reload to show updated data
            },
            { error ->
                Toast.makeText(this, "Failed to save project data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadProjectDataFromDatabase() {
        val request = JsonArrayRequest(
            Request.Method.GET, serverUrl, null,
            { response ->
                parseAndLoadProjectData(response)
            },
            { error ->
                Toast.makeText(this, "Failed to load project data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseAndLoadProjectData(response: JSONArray) {
        projectList.clear()
        for (i in 0 until response.length()) {
            val projectJson = response.getJSONObject(i)
            val project = Project(
                projectTitle = projectJson.getString("projectTitle"),
                projectDescription = projectJson.getString("projectDescription")
            )
            projectList.add(project)
        }
        projectAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        etProjTitle.text.clear()
        etProjDescription.text.clear()
    }

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
