package com.example.resumaker

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ObjectiveActivity : AppCompatActivity() {

    private lateinit var etObjective: EditText
    private lateinit var btnSaveObjective: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var objectiveAdapter: ObjectiveAdapter
    private var objectiveList = mutableListOf<Objective>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.objective)

        etObjective = findViewById(R.id.et_obj)
        btnSaveObjective = findViewById(R.id.btn_save_obj)
        ibtnBack = findViewById(R.id.ibtn_back5)
        recyclerView = findViewById(R.id.recyclerView_objective)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        objectiveAdapter = ObjectiveAdapter(objectiveList)
        recyclerView.adapter = objectiveAdapter

        // Load objectives from saved data
        loadObjectives()

        btnSaveObjective.setOnClickListener {
            val objectiveText = etObjective.text.toString()
            if (objectiveText.isNotEmpty()) {
                saveObjectiveData(objectiveText)
                Toast.makeText(this, "Objective saved successfully", Toast.LENGTH_SHORT).show()
                etObjective.text.clear()
                loadObjectives() // Refresh the RecyclerView to show updated objectives
            } else {
                etObjective.error = "Field is empty"
            }
        }

        ibtnBack.setOnClickListener {
            finish()
        }
    }

    private fun saveObjectiveData(objectiveText: String) {
        val resumeData = loadResumeData()
        resumeData.objectives.add(Objective(objectiveText))
        saveResumeData(resumeData)
    }

    private fun loadObjectives() {
        val resumeData = loadResumeData()
        objectiveList.clear()
        objectiveList.addAll(resumeData.objectives)
        objectiveAdapter.notifyDataSetChanged()
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
}
