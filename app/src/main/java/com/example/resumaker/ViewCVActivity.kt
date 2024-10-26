package com.example.resumaker

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class ViewCVActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_cv)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        // Load ResumeData from SharedPreferences
        val resumeData = loadResumeData()

        // Load and bind the HTML template
        val htmlContent = loadHtmlTemplate()
        val finalHtml = bindResumeData(htmlContent, resumeData)

        webView.loadDataWithBaseURL(null, finalHtml, "text/html", "UTF-8", null)
    }

    private fun loadHtmlTemplate(): String {
        val inputStream = assets.open("resume1classic.html")
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun loadResumeData(): ResumeData {
        val sharedPreferences = getSharedPreferences("ResumeData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("resumeData", null)
        return if (json != null) {
            gson.fromJson(json, ResumeData::class.java)
        } else {
            ResumeData()  // Provide default data if nothing is found
        }
    }

    private fun bindResumeData(template: String, data: ResumeData): String {
        return template
            .replace("{name}", data.personalDetails.getOrNull(0)?.name ?: "N/A")
            .replace("{desiredJob}", data.personalDetails.getOrNull(0)?.desiredJob ?: "N/A")
            .replace("{email}", data.personalDetails.getOrNull(0)?.email ?: "N/A")
            .replace("{address}", data.personalDetails.getOrNull(0)?.address ?: "N/A")
            .replace("{phone}", data.personalDetails.getOrNull(0)?.phone ?: "N/A")
            .replace("{linkedin}", data.personalDetails.getOrNull(0)?.linkedIn ?: "N/A")
            .replace("{objective}", formatObjectives(data.objectives))
            .replace("{education}", formatEducation(data.education))
            .replace("{experience}", formatExperience(data.experience))
            .replace("{skills}", formatSkills(data.skills))
            .replace("{awards}", formatAwards(data.awards))
            .replace("{projects}", formatProjects(data.projects))
    }

    private fun formatObjectives(objectivesList: List<Objective>): String {
        return if (objectivesList.isEmpty()) {
            "<p>No objective listed.</p>"
        } else {
            objectivesList.joinToString(separator = "<br>") { it.objectiveText }
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
            awardsList.joinToString(separator = "<br>") { "${it.award} - ${it.awardDescription}" }
        }
    }

    private fun formatProjects(projectList: List<Project>): String {
        return if (projectList.isEmpty()) {
            "<p>No projects listed.</p>"
        } else {
            projectList.joinToString(separator = "<br>") {
                "${it.projectTitle}<br>Description: ${it.projectDescription}"
            }
        }
    }
}
