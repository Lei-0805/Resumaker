package com.example.resumaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class createpage : AppCompatActivity() {

    private lateinit var ibtnPersonal: ImageButton
    private lateinit var ibtnEducation: ImageButton
    private lateinit var ibtnExperience: ImageButton
    private lateinit var ibtnSkills: ImageButton
    private lateinit var ibtnObjective: ImageButton
    private lateinit var ibtnProjects: ImageButton
    private lateinit var ibtnAwards: ImageButton
    private lateinit var ibtnBackCreatepage: ImageButton
    private lateinit var btnViewCv: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createpage)

        // Initialize buttons
        ibtnPersonal = findViewById(R.id.ibtn_personal)
        ibtnEducation = findViewById(R.id.ibtn_education)
        ibtnExperience = findViewById(R.id.ibtn_experience)
        ibtnSkills = findViewById(R.id.ibtn_skills)
        ibtnObjective = findViewById(R.id.ibtn_objective)
        ibtnProjects = findViewById(R.id.ibtn_projects)
        ibtnAwards = findViewById(R.id.ibtn_awards)
        ibtnBackCreatepage = findViewById(R.id.ibtnBackCreatepage)
        btnViewCv = findViewById(R.id.btn_viewcv)

        // Set up click listeners for each button
        ibtnPersonal.setOnClickListener { navigateToActivity(PersonalDetailsActivity::class.java) }
        ibtnEducation.setOnClickListener { navigateToActivity(EducationActivity::class.java) }
        ibtnExperience.setOnClickListener { navigateToActivity(ExperienceActivity::class.java) }
        ibtnSkills.setOnClickListener { navigateToActivity(SkillActivity::class.java) }
        ibtnObjective.setOnClickListener { navigateToActivity(ObjectiveActivity::class.java) }
        ibtnProjects.setOnClickListener { navigateToActivity(ProjectActivity::class.java) }
        ibtnAwards.setOnClickListener { navigateToActivity(AwardActivity::class.java) }

        btnViewCv.setOnClickListener {
            val resumeData = gatherResumeData()
            navigateToViewCVActivity(resumeData)
        }
        ibtnBackCreatepage.setOnClickListener{
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

    private fun navigateToActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }

    private fun gatherResumeData(): ResumeData {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)

        // Load personal details
        val personalDetails = PersonalDetail(
            name = sharedPreferences.getString("name", "").orEmpty(),
            desiredJob = sharedPreferences.getString("desiredJob", "").orEmpty(),
            address = sharedPreferences.getString("address", "").orEmpty(),
            email = sharedPreferences.getString("email", "").orEmpty(),
            phone = sharedPreferences.getString("phone", "").orEmpty(),
            linkedIn = sharedPreferences.getString("linkedIn", "").orEmpty(),
        )

        // Load other resume data
        val educationList = loadDataFromSharedPreferences<Education>("educationList")
        val experienceList = loadDataFromSharedPreferences<Experience>("experienceList")
        val skillsList = loadDataFromSharedPreferences<Skill>("skillsList")
        val objectiveText = sharedPreferences.getString("objective", "").orEmpty()
        val awardsList = loadDataFromSharedPreferences<Award>("awardsList")
        val projectList = loadDataFromSharedPreferences<Project>("projectList")

        // Wrap objectiveText in a list of Objective objects
        val objectivesList = if (objectiveText.isNotEmpty()) {
            listOf(Objective(objectiveText))
        } else {
            emptyList()
        }

        return ResumeData(
            personalDetails = listOf(personalDetails),
            education = educationList,
            experience = experienceList,
            skills = skillsList,
            objectives = objectivesList.toMutableList(),
            projects = projectList,
            awards = awardsList
        )
    }

    private fun navigateToViewCVActivity(resumeData: ResumeData) {
        val intent = Intent(this, ViewCVActivity::class.java)
        // You may need to convert resumeData to JSON or make it Parcelable if needed
        intent.putExtra("resume_data", resumeData)
        startActivity(intent)
    }

    private inline fun <reified T> loadDataFromSharedPreferences(key: String): MutableList<T> {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val json = sharedPreferences.getString(key, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<T>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}
