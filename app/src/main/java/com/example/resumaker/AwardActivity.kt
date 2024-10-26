package com.example.resumaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class AwardActivity : AppCompatActivity() {

    private lateinit var etAchievement: EditText
    private lateinit var etAwardDescription: EditText
    private lateinit var btnSaveAward: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var awardAdapter: AwardAdapter
    private var awardList: MutableList<Award> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.award)

        etAchievement = findViewById(R.id.et_award_title)
        etAwardDescription = findViewById(R.id.et_award_description)
        btnSaveAward = findViewById(R.id.btn_save_award)
        ibtnBack = findViewById(R.id.ibtn_back2)
        recyclerView = findViewById(R.id.recyclerView_awards)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        awardAdapter = AwardAdapter(awardList)
        recyclerView.adapter = awardAdapter

        loadAwardData()  // Load existing awards

        btnSaveAward.setOnClickListener {
            if (areFieldsValid()) {
                saveAwardData()
                Toast.makeText(this, "Award saved successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                loadAwardData()  // Reload awards to show the new one
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
            etAchievement.text.isNullOrEmpty() -> {
                etAchievement.error = "Field is empty"
                false
            }
            etAwardDescription.text.isNullOrEmpty() -> {
                etAwardDescription.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    private fun saveAwardData() {
        val achievement = etAchievement.text.toString()
        val awardDescription = etAwardDescription.text.toString()

        // Create an instance of the Award data class
        val awardEntry = Award(award = achievement, awardDescription = awardDescription)

        // Update the award list
        awardList.add(awardEntry)

        // Load current ResumeData
        val resumeData = loadResumeData()

        // Update the ResumeData with the new award entry
        resumeData.awards.add(awardEntry)

        // Save updated ResumeData
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString("resumeData", gson.toJson(resumeData))
        editor.apply()

        // Notify the adapter of the new item
        awardAdapter.notifyItemInserted(awardList.size - 1)
    }

    private fun loadAwardData() {
        val resumeData = loadResumeData()
        awardList.clear()
        awardList.addAll(resumeData.awards)
        awardAdapter.notifyDataSetChanged()  // Refresh the RecyclerView
    }

    private fun loadResumeData(): ResumeData {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)
        return if (json != null) {
            gson.fromJson(json, ResumeData::class.java)
        } else {
            ResumeData()  // Provide default data if nothing is found
        }
    }

    private fun clearFields() {
        etAchievement.text.clear()
        etAwardDescription.text.clear()
    }
}
