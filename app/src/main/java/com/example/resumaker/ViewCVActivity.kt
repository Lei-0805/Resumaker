package com.example.resumaker

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class ViewCVActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
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

        // Setup download button
        val downloadButton: ImageButton = findViewById(R.id.download_button)
        downloadButton.setOnClickListener {
            createPdfFromWebView()
        }
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

    private fun createPdfFromWebView() {
        webView.webViewClient = object : WebViewClient() {
            @SuppressLint("NewApi")
            override fun onPageFinished(view: WebView?, url: String?) {
                val pdfFileName = "Resume_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())}.pdf"
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = contentResolver
                val pdfUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (pdfUri != null) {
                    resolver.openOutputStream(pdfUri)?.use { outputStream ->
                        // Create a PdfDocument
                        val pdfDocument = PdfDocument()
                        val pageInfo = PdfDocument.PageInfo.Builder(webView.width, webView.height, 1).create()
                        val page = pdfDocument.startPage(pageInfo)

                        // Draw the webView content on the PDF page's canvas
                        val canvas = page.canvas
                        webView.draw(canvas)

                        // Finish the page and write to the output stream
                        pdfDocument.finishPage(page)
                        pdfDocument.writeTo(outputStream)
                        pdfDocument.close()

                        Toast.makeText(this@ViewCVActivity, "Resume downloaded as PDF in Downloads folder", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@ViewCVActivity, "Failed to create file", Toast.LENGTH_SHORT).show()
                }
            }
        }

        webView.reload()  // Ensure the WebView content is fully rendered before capturing as PDF
    }
}
