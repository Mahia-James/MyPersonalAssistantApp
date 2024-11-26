package com.example.mypersonalassistant

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GeneralInfoActivity : AppCompatActivity() {
    private lateinit var etQuery: EditText
    private lateinit var btnSubmitQuery: Button
    private lateinit var tvQueryResult: TextView

    companion object {
        const val API_KEY = "KQRAWU-YXVYUK6ARA" // Replace with your Wolfram Alpha API key
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_info)

        etQuery = findViewById(R.id.etQuery)
        btnSubmitQuery = findViewById(R.id.btnSubmitQuery)
        tvQueryResult = findViewById(R.id.tvQueryResult)

        btnSubmitQuery.setOnClickListener {
            val query = etQuery.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchGeneralInformation(query)
            } else {
                tvQueryResult.text = getString(R.string.enter_your_query)
            }
        }
    }

    private fun fetchGeneralInformation(query: String) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                fetchDataFromWolframAlpha(query)
            }
            tvQueryResult.text = result
        }
    }

    private suspend fun fetchDataFromWolframAlpha(query: String): String {
        return try {
            // Utilize the create() function from WolframAlphaApi
            val wolframAlphaApi = WolframAlphaApi.create()
            val response = wolframAlphaApi.getAnswer(query, API_KEY)

            if (response.isSuccessful && response.body() != null) {
                val queryResult = response.body()!!.queryResult // Note: updated to match your model property name
                if (queryResult.success && !queryResult.pods.isNullOrEmpty()) {
                    val resultBuilder = StringBuilder()
                    for (pod in queryResult.pods) {
                        resultBuilder.append("${pod.title}:\n")
                        for (subpod in pod.subpods) {
                            resultBuilder.append("${subpod.plaintext}\n")
                        }
                        resultBuilder.append("\n")
                    }
                    resultBuilder.toString().trim()
                } else {
                    "No information found for the query."
                }
            } else {
                "No information found for the query."
            }
        } catch (e: Exception) {
            "Error fetching information: ${e.message}"
        }
    }
}
