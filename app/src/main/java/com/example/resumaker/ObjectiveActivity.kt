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
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class ObjectiveActivity : AppCompatActivity() {

    private lateinit var etObjective: EditText
    private lateinit var ibtnBackObjective: ImageButton
    private lateinit var btnSaveObjective: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var objectiveAdapter: ObjectiveAdapter
    private lateinit var requestQueue: RequestQueue

    private var objectiveList = mutableListOf<Objective>()
    private val serverUrl = "http://192.168.13.6:8000/api/objective_data" // Replace with your actual API endpoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective)

        etObjective = findViewById(R.id.et_obj)
        ibtnBackObjective = findViewById(R.id.ibtnBackObjective)
        btnSaveObjective = findViewById(R.id.btn_save_obj)
        recyclerView = findViewById(R.id.recyclerView_objective)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        objectiveAdapter = ObjectiveAdapter(objectiveList)
        recyclerView.adapter = objectiveAdapter

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Set validation filters
        setValidationFilters()

        // Load objectives from the database
        loadObjectivesFromDatabase()

        btnSaveObjective.setOnClickListener {
            if (etObjective.text.isNotEmpty()) {
                saveObjectiveData()
            } else {
                etObjective.error = "Field is empty"
            }
        }

        ibtnBackObjective.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etObjective.filters = arrayOf(noNumbersFilter)
    }

    private fun saveObjectiveData() {
        val objectiveText = etObjective.text.toString()
        val objective = Objective(objectiveText = objectiveText)

        // Add to local list and save in SharedPreferences
        objectiveList.add(objective)
        saveResumeData()

        // Send the objective to the backend
        sendObjectiveToDatabase(objective)
        etObjective.text.clear()
        loadObjectivesFromDatabase() // Refresh RecyclerView
    }

    private fun sendObjectiveToDatabase(objective: Objective) {
        val params = JSONObject()
        params.put("objectiveText", objective.objectiveText)

        val request = JsonObjectRequest(
            Request.Method.POST, serverUrl, params,
            { Toast.makeText(this, "Objective saved successfully", Toast.LENGTH_SHORT).show() },
            { error -> Toast.makeText(this, "Failed to save objective data", Toast.LENGTH_LONG).show() }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadObjectivesFromDatabase() {
        val request = JsonArrayRequest(
            Request.Method.GET, serverUrl, null,
            { response -> parseAndLoadObjectiveData(response) },
            { error -> Toast.makeText(this, "Failed to load objective data", Toast.LENGTH_LONG).show() }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    private fun parseAndLoadObjectiveData(response: JSONArray) {
        objectiveList.clear()
        for (i in 0 until response.length()) {
            val objectiveJson = response.getJSONObject(i)
            val objective = Objective(objectiveText = objectiveJson.getString("objectiveText"))
            objectiveList.add(objective)
        }
        objectiveAdapter.notifyDataSetChanged()
    }

    private fun saveResumeData() {
        val resumeData = loadResumeData()
        resumeData.objectives = objectiveList
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

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
