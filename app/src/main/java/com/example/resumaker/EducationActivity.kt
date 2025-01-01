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

class EducationActivity : AppCompatActivity() {

    private lateinit var etProgram: EditText
    private lateinit var etSchool: EditText
    private lateinit var etSchoolYear: EditText
    private lateinit var ibtnBackEducation: ImageButton
    private lateinit var btnSaveEduc: Button
    private lateinit var educationAdapter: EducationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var requestQueue: RequestQueue

    private var educationList = mutableListOf<Education>()
    private val serverUrl = "http://192.168.13.6:8000/api/education_data" // Replace with actual URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.education)

        etProgram = findViewById(R.id.et_program)
        etSchool = findViewById(R.id.et_school)
        etSchoolYear = findViewById(R.id.et_schoolyear_college)
        ibtnBackEducation = findViewById(R.id.ibtnBackEducation)
        btnSaveEduc = findViewById(R.id.btn_save_educ)
        recyclerView = findViewById(R.id.recyclerView_education)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        educationAdapter = EducationAdapter(educationList)
        recyclerView.adapter = educationAdapter

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Set validation filters
        setValidationFilters()

        // Load existing education data from backend
        loadEducationDataFromDatabase()

        btnSaveEduc.setOnClickListener {
            if (areFieldsValid()) {
                saveEducationDataToDatabase()
                clearFields()
            }
        }

        ibtnBackEducation.setOnClickListener {
            navigateTo(createpage::class.java)
        }
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etProgram.filters = arrayOf(noNumbersFilter)
        etSchool.filters = arrayOf(noNumbersFilter)
        etSchoolYear.filters = arrayOf(InputFilter.LengthFilter(9))
    }

    private fun areFieldsValid(): Boolean {
        return when {
            etProgram.text.isNullOrEmpty() -> {
                etProgram.error = "Field is empty"
                false
            }
            etSchool.text.isNullOrEmpty() -> {
                etSchool.error = "Field is empty"
                false
            }
            etSchoolYear.text.isNullOrEmpty() -> {
                etSchoolYear.error = "Field is empty"
                false
            }
            else -> true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveEducationDataToDatabase() {
        val program = etProgram.text.toString()
        val school = etSchool.text.toString()
        val schoolYear = etSchoolYear.text.toString()

        // JSON payload
        val params = JSONObject()
        params.put("program", program)
        params.put("school", school)
        params.put("schoolYear", schoolYear)

        // Volley POST request
        val request = JsonObjectRequest(
            Request.Method.POST, serverUrl, params,
            { response ->
                Toast.makeText(this, "Education saved successfully", Toast.LENGTH_SHORT).show()
                loadEducationDataFromDatabase() // Reload to show updated data
            },
            { error ->
                Toast.makeText(this, "Failed to save education data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadEducationDataFromDatabase() {
        val request = JsonArrayRequest(
            Request.Method.GET, serverUrl, null,
            { response ->
                parseAndLoadEducationData(response)
            },
            { error ->
                Toast.makeText(this, "Failed to load education data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    private fun parseAndLoadEducationData(response: JSONArray) {
        educationList.clear()
        for (i in 0 until response.length()) {
            val educationJson = response.getJSONObject(i)
            val education = Education(
                program = educationJson.getString("program"),
                school = educationJson.getString("school"),
                schoolYear = educationJson.getString("schoolYear")
            )
            educationList.add(education)
        }
        educationAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        etProgram.text.clear()
        etSchool.text.clear()
        etSchoolYear.text.clear()
    }

    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
