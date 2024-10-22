package com.example.resumaker

import Award
import Education
import Experience
import Objective
import PersonalDetail
import Project
import ResumeData
import Skill
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class ViewCVActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var resumeData: ResumeData

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_cv)

        // Initialize WebView
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        // Retrieve resume data passed from the previous activity
        resumeData = intent.getParcelableExtra("resume_data") ?: createEmptyResumeData()

        // Debug log to check the received resume data
        Log.d("ViewCVActivity", "Resume Data: $resumeData")

        // Load the HTML template and bind resume data
        val htmlContent = loadHtmlTemplate()
        val finalHtml = bindResumeData(htmlContent, resumeData)

        // Display the modified HTML in WebView
        webView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null)
    }

    private fun createEmptyResumeData(): ResumeData {
        // Create an empty ResumeData object with default values
        return ResumeData(
            personalDetails = listOf(PersonalDetail("", "", "", "", "", "")),
            education = emptyList(),
            experience = emptyList(),
            skills = emptyList(),
            objective = Objective(""),
            projects = emptyList(),
            awards = emptyList()
        )
    }

    private fun loadHtmlTemplate(): String {
        // Load the HTML template from the assets directory
        val inputStream = assets.open("resume1classic.html")
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun bindResumeData(template: String, data: ResumeData): String {
        return template
            .replace("{name}", data.personalDetails.getOrNull(0)?.name ?: "N/A")
            .replace("{desiredJob}", data.personalDetails.getOrNull(0)?.desiredJob ?: "N/A")
            .replace("{email}", data.personalDetails.getOrNull(0)?.email ?: "N/A")
            .replace("{address}", data.personalDetails.getOrNull(0)?.address ?: "N/A")
            .replace("{phone}", data.personalDetails.getOrNull(0)?.phone ?: "N/A")
            .replace("{linkedin}", data.personalDetails.getOrNull(0)?.linkedIn ?: "N/A")
            .replace("{objective}", formatObjective(data.objective))
            .replace("{education}", formatEducation(data.education))
            .replace("{experience}", formatExperience(data.experience))
            .replace("{skills}", formatSkills(data.skills))
            .replace("{awards}", formatAwards(data.awards))
            .replace("{projects}", formatProjects(data.projects))
    }

    private fun formatObjective(objective: Objective): String {
        return objective.objectiveText.ifEmpty {
            "<p>No objective listed.</p>"
        }
    }

    private fun formatEducation(educationList: List<Education>): String {
        return if (educationList.isEmpty()) {
            "<p>No education listed.</p>"
        } else {
            educationList.joinToString(separator = "<br>") {
                "${it.program} at ${it.school} (${it.schoolYear})"
            }
        }
    }

    private fun formatExperience(experienceList: List<Experience>): String {
        return if (experienceList.isEmpty()) {
            "<p>No experience listed.</p>"
        } else {
            experienceList.joinToString(separator = "<br>") {
                "${it.jobTitle} at ${it.company} (${it.startDate} - ${it.endDate})<br>${it.details}"
            }
        }
    }

    private fun formatSkills(skillsList: List<Skill>): String {
        return if (skillsList.isEmpty()) {
            "<p>No skills listed.</p>"
        } else {
            skillsList.joinToString(separator = ", ") { it.skillName }
        }
    }

    private fun formatAwards(awardsList: List<Award>): String {
        return if (awardsList.isEmpty()) {
            "<p>No awards listed.</p>"
        } else {
            awardsList.joinToString(separator = "<br>") { "${it.achievement} - ${it.description}" }
        }
    }

    private fun formatProjects(projectList: List<Project>): String {
        return if (projectList.isEmpty()) {
            "<p>No projects listed.</p>"
        } else {
            projectList.joinToString(separator = "<br>") {
                "${it.title}<br>Description: ${it.description}"
            }
        }
    }
}
