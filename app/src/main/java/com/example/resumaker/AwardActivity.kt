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

class AwardActivity : AppCompatActivity() {

    private lateinit var etAchievement: EditText
    private lateinit var etAwardDescription: EditText
    private lateinit var ibtnBackAward: ImageButton
    private lateinit var btnSaveAward: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var awardAdapter: AwardAdapter
    private lateinit var requestQueue: RequestQueue

    private var awardList: MutableList<Award> = mutableListOf()
    private val serverUrl = "http://192.168.13.6:8000/api/award_data" // Replace with your actual endpoint

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.award)

        etAchievement = findViewById(R.id.et_award_title)
        etAwardDescription = findViewById(R.id.et_award_description)
        ibtnBackAward = findViewById(R.id.ibtnBackAward)
        btnSaveAward = findViewById(R.id.btn_save_award)
        recyclerView = findViewById(R.id.recyclerView_awards)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        awardAdapter = AwardAdapter(awardList)
        recyclerView.adapter = awardAdapter

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Set validation filters
        setValidationFilters()

        // Load existing awards from database
        loadAwardsFromDatabase()

        btnSaveAward.setOnClickListener {
            if (areFieldsValid()) {
                saveAwardToDatabase()
                clearFields()
            }
        }
        ibtnBackAward.setOnClickListener{
            navigateTo(createpage::class.java)
        }
    }

    // Function to navigate to the next page
    private fun navigateTo(nextPage: Class<*>) {
        val intent = Intent(this, nextPage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun setValidationFilters() {
        val noNumbersFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]"))) "" else null
        }
        etAchievement.filters = arrayOf(noNumbersFilter)
        etAwardDescription.filters = arrayOf(noNumbersFilter)
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

    private fun saveAwardToDatabase() {
        val achievement = etAchievement.text.toString()
        val awardDescription = etAwardDescription.text.toString()

        // Create JSON payload for request
        val params = JSONObject()
        params.put("award", achievement)
        params.put("awardDescription", awardDescription)

        // Volley POST request to save award
        val request = JsonObjectRequest(
            Request.Method.POST, serverUrl, params,
            { response ->
                Toast.makeText(this, "Award saved successfully", Toast.LENGTH_SHORT).show()
                loadAwardsFromDatabase() // Refresh awards list
            },
            { error ->
                Toast.makeText(this, "Failed to save awards data", Toast.LENGTH_LONG)
                    .show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    private fun loadAwardsFromDatabase() {
        val request = JsonArrayRequest(
            Request.Method.GET, serverUrl, null,
            { response ->
                parseAndLoadAwards(response)
            },
            { error ->
                Toast.makeText(this, "Failed to load awards data", Toast.LENGTH_LONG).show()
            }
        )

        // Add request to queue
        requestQueue.add(request)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun parseAndLoadAwards(response: JSONArray) {
        awardList.clear()
        for (i in 0 until response.length()) {
            val awardJson = response.getJSONObject(i)
            val award = Award(
                award = awardJson.getString("award"),
                awardDescription = awardJson.getString("awardDescription")
            )
            awardList.add(award)
        }
        awardAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        etAchievement.text.clear()
        etAwardDescription.text.clear()
    }
}
