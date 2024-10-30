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
import com.google.gson.Gson

class ObjectiveActivity : AppCompatActivity() {

    private lateinit var etObjective: EditText
    private lateinit var ibtnBackObjective: ImageButton
    private lateinit var btnSaveObjective: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var objectiveAdapter: ObjectiveAdapter
    private var objectiveList = mutableListOf<Objective>()

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

        // Set validation filters
        setValidationFilters()

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
        ibtnBackObjective.setOnClickListener{
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
        //Prevent numbers to be entered in etObjective
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        //Set the filter that do not allow numbers
        etObjective.filters = arrayOf(noNumbersFilter)
    }

    private fun saveObjectiveData(objectiveText: String) {
        val resumeData = loadResumeData()
        resumeData.objectives.add(Objective(objectiveText))
        saveResumeData(resumeData)
    }

    @SuppressLint("NotifyDataSetChanged")
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
