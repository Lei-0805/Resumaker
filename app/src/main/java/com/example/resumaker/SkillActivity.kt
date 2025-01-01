package com.example.resumaker

import android.annotation.SuppressLint
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class SkillActivity : AppCompatActivity() {

    private lateinit var etSkill: EditText
    private lateinit var ibtnBackSkill: ImageButton
    private lateinit var btnSaveSkill: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var skillAdapter: SkillAdapter
    private lateinit var requestQueue: RequestQueue

    private var skillList = mutableListOf<Skill>()
    private val serverUrl = "http://192.168.13.6:8000/api/skill_data" // Replace with actual URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.skill)

        // Initialize UI components
        etSkill = findViewById(R.id.et_skill)
        ibtnBackSkill = findViewById(R.id.ibtnBackSkill)
        btnSaveSkill = findViewById(R.id.btn_save_skill)
        recyclerView = findViewById(R.id.recyclerView_skill)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        skillAdapter = SkillAdapter(skillList)
        recyclerView.adapter = skillAdapter

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Set validation filters
        setValidationFilters()

        // Load existing skill data from backend
        loadSkillDataFromDatabase()

        btnSaveSkill.setOnClickListener {
            if (areFieldsValid()) {
                saveSkillDataToDatabase()
                clearFields()
            }
        }

        ibtnBackSkill.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etSkill.filters = arrayOf(noNumbersFilter)
    }

    private fun areFieldsValid(): Boolean {
        return if (etSkill.text.isNullOrEmpty()) {
            etSkill.error = "Field is empty"
            false
        } else {
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveSkillDataToDatabase() {
        val skillName = etSkill.text.toString()

        // JSON payload
        val params = JSONObject()
        params.put("skillName", skillName)

        // Volley POST request
        val request = JsonObjectRequest(
            Request.Method.POST, serverUrl, params,
            { response ->
                Toast.makeText(this, "Skill saved successfully", Toast.LENGTH_SHORT).show()
                loadSkillDataFromDatabase() // Reload to show updated data
            },
            { error ->
                Toast.makeText(this, "Failed to save skill data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadSkillDataFromDatabase() {
        val request = JsonArrayRequest(
            Request.Method.GET, serverUrl, null,
            { response ->
                parseAndLoadSkillData(response)
            },
            { error ->
                Toast.makeText(this, "Failed to load skill data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseAndLoadSkillData(response: JSONArray) {
        skillList.clear()
        for (i in 0 until response.length()) {
            val skillJson = response.getJSONObject(i)
            val skill = Skill(
                skillName = skillJson.getString("skillName")
            )
            skillList.add(skill)
        }
        skillAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        etSkill.text.clear()
    }

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
