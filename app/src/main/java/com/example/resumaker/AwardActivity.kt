package com.example.resumaker

import Award
import android.annotation.SuppressLint
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
import com.google.gson.reflect.TypeToken

class AwardActivity : AppCompatActivity() {

    private lateinit var etAchievement: EditText
    private lateinit var etAwardDescription: EditText
    private lateinit var btnSaveAward: Button
    private lateinit var ibtnBack: ImageButton
    private lateinit var recyclerViewAwards: RecyclerView
    private lateinit var awardAdapter: AwardAdapter

    private var awardList = mutableListOf<Award>() // To hold the award data

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.award)

        etAchievement = findViewById(R.id.et_achievement)
        etAwardDescription = findViewById(R.id.et_award_description)
        btnSaveAward = findViewById(R.id.btn_save_award)
        ibtnBack = findViewById(R.id.ibtn_back2)
        recyclerViewAwards = findViewById(R.id.recyclerView_awards)

        // Load existing award data
        loadAwardData()

        // Set up RecyclerView
        awardAdapter = AwardAdapter(awardList)
        recyclerViewAwards.layoutManager = LinearLayoutManager(this)
        recyclerViewAwards.adapter = awardAdapter

        btnSaveAward.setOnClickListener {
            if (areFieldsValid()) {
                submitAwardData()
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

    private fun submitAwardData() {
        val achievement = etAchievement.text.toString()
        val awardDescription = etAwardDescription.text.toString() // Changed from yearReceivedStr to awardDescription

        // Create an instance of the Award data class
        val awardEntry = Award(achievement = achievement, description = awardDescription) // Use awardDescription

        // Add the new award entry to the award list
        awardList.add(awardEntry)

        // Save updated award data
        saveAwardData()

        // Feedback to the user
        Toast.makeText(this, "Award saved successfully", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearFields()

        // Notify the adapter of the new data
        awardAdapter.notifyItemInserted(awardList.size - 1)  // Notify only the inserted item
    }

    private fun saveAwardData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the awardList to a JSON string using Gson
        val gson = Gson()
        val json = gson.toJson(awardList)

        // Save the JSON string in SharedPreferences
        editor.putString("awardList", json)
        editor.apply()
    }

    private fun loadAwardData() {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("awardList", null)

        if (json != null) {
            // Deserialize the JSON string back to a MutableList<Award>
            val type = object : TypeToken<MutableList<Award>>() {}.type
            awardList = gson.fromJson(json, type) ?: mutableListOf()
        }
    }

    private fun clearFields() {
        etAchievement.text.clear()
        etAwardDescription.text.clear() // Change to clear etAwardDescription
    }
}
