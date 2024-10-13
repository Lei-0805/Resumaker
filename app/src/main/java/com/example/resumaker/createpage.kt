package com.example.resumaker


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class createpage : AppCompatActivity() {

    lateinit var ibtn_personal: ImageButton
    lateinit var ibtn_education: ImageButton
    lateinit var ibtn_experience: ImageButton
    lateinit var ibtn_skills: ImageButton
    lateinit var ibtn_objective: ImageButton
    lateinit var ibtn_reference: ImageButton
    lateinit var ibtn_projects: ImageButton
    lateinit var ibtn_awards: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createpage)

        ibtn_personal = findViewById(R.id.ibtn_personal)
        ibtn_education = findViewById(R.id.ibtn_education)
        ibtn_experience = findViewById(R.id.ibtn_experience)
        ibtn_skills = findViewById(R.id.ibtn_skills)
        ibtn_objective = findViewById(R.id.ibtn_objective)
        ibtn_reference = findViewById(R.id.ibtn_reference)
        ibtn_projects = findViewById(R.id.ibtn_projects)
        ibtn_awards = findViewById(R.id.ibtn_awards)

        ibtn_personal.setOnClickListener {
            val intent = Intent(this, personaldetails::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_education.setOnClickListener {
            val intent = Intent(this, education::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_experience.setOnClickListener {
            val intent = Intent(this, experience::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_skills.setOnClickListener {
            val intent = Intent(this, skills::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_objective.setOnClickListener {
            val intent = Intent(this, objective::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_reference.setOnClickListener {
            val intent = Intent(this, reference::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_projects.setOnClickListener {
            val intent = Intent(this, project::class.java)
            startActivity(intent)
            finish()
        }

        ibtn_awards.setOnClickListener {
            val intent = Intent(this, awards::class.java)
            startActivity(intent)
            finish()
        }
    }
}