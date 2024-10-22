package com.example.resumaker

import Objective
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

class ObjectiveActivity : AppCompatActivity() {

    private lateinit var etObjective: EditText
    private lateinit var btnSaveObjective: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerViewObjectives: RecyclerView
    private lateinit var objectiveAdapter: ObjectiveAdapter

    private var objectiveList = mutableListOf<Objective>() // To hold the objective data

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.objective)

        etObjective = findViewById(R.id.et_obj)
        btnSaveObjective = findViewById(R.id.btn_save_obj)
        ibtnBack = findViewById(R.id.ibtn_back5)
        recyclerViewObjectives = findViewById(R.id.recyclerView_objective)

        // Load existing objective data
        loadObjectiveData()

        // Set up RecyclerView
        objectiveAdapter = ObjectiveAdapter(objectiveList)
        recyclerViewObjectives.layoutManager = LinearLayoutManager(this)
        recyclerViewObjectives.adapter = objectiveAdapter

        // Save objective button click listener
        btnSaveObjective.setOnClickListener {
            if (areFieldsValid()) {
                submitObjectiveData()
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
        return if (etObjective.text.isNullOrEmpty()) {
            etObjective.error = "Field is empty"
            false
        } else {
            true
        }
    }

    // Submit objective data
    private fun submitObjectiveData() {
        val objectiveText = etObjective.text.toString()

        // Create an objective entry
        val objectiveEntry = Objective(objectiveText)

        // Add the new objective entry to the list
        objectiveList.add(objectiveEntry)

        // Save updated objective data
        saveObjectiveData()

        // Show success message
        Toast.makeText(this, "Objective saved successfully", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()

        // Notify the adapter of the new item inserted
        objectiveAdapter.notifyItemInserted(objectiveList.size - 1)
    }

    // Save objective data to SharedPreferences
    private fun saveObjectiveData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the objectiveList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(this.objectiveList)

        // Save the JSON string in SharedPreferences
        editor.putString("objectiveList", json)
        editor.apply()
    }

    // Load objective data from SharedPreferences
    private fun loadObjectiveData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("objectiveList", null)

        if (json != null) {
            // Deserialize the JSON string back to a MutableList<Objective>
            val type = object : TypeToken<MutableList<Objective>>() {}.type
            objectiveList = gson.fromJson(json, type) ?: mutableListOf()
        }
    }

    // Clear input fields
    private fun clearFields() {
        etObjective.text.clear()
    }
}
