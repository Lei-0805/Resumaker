package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class createdprofiles : AppCompatActivity() {

    private lateinit var profile_name: TextView
    private lateinit var profile_desired_job: TextView
    private lateinit var ibtnBackCreatedProfiles: ImageButton
    private lateinit var btn_edit: Button
    private lateinit var btn_delete: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createdprofiles)

        profile_name = findViewById(R.id.profile_name)
        profile_desired_job = findViewById(R.id.profile_desired_job)
        ibtnBackCreatedProfiles = findViewById(R.id.ibtnBackCreatedProfiles)
        btn_edit = findViewById(R.id.btn_edit)
        btn_delete = findViewById(R.id.btn_delete)

        loadProfileData()

        btn_edit.setOnClickListener {
            val intent = Intent(this, createpage::class.java)
            startActivity(intent)
            finish()
        }

        btn_delete.setOnClickListener {
            deleteResumeData()
        }
        ibtnBackCreatedProfiles.setOnClickListener{
            navigateTo(navfunction::class.java)
        }
    }

    // Function to navigate to the next page
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun loadProfileData() {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)

        if (json != null) {
            val resumeData = gson.fromJson(json, ResumeData::class.java)
            profile_name.text = resumeData.personalDetails.getOrNull(0)?.name ?: "Name"
            profile_desired_job.text = resumeData.personalDetails.getOrNull(0)?.desiredJob ?: "Desired Job"
        } else {
            profile_name.text = "Name"
            profile_desired_job.text = "Desired Job"
        }
    }

    private fun deleteResumeData() {
        val sharedPreferences = getSharedPreferences("ResumeData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // This removes all saved data under "ResumeData"
        editor.apply()

        Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()

        finish() // Close the current activity
    }
}
