package com.example.mypersonalassistant

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is logged in
        val sharedPref: SharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE)
        val loggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (!loggedIn) {
            // Redirect to Login activity if not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        // Display welcome message with user's name
        val userName = sharedPref.getString("name", "User")
        val tvWelcome: TextView = findViewById(R.id.tvWelcome)
        tvWelcome.text = getString(R.string.welcome_message, userName)

        val fabTaskManagement: ExtendedFloatingActionButton = findViewById(R.id.fabTaskManagement)
        val fabEventScheduling: ExtendedFloatingActionButton = findViewById(R.id.fabEventScheduling)
        val fabWeatherUpdates: ExtendedFloatingActionButton = findViewById(R.id.fabWeatherUpdates)
        val fabGeneralInfo: ExtendedFloatingActionButton = findViewById(R.id.fabGeneralInfo)
        val btnLogout: Button = findViewById(R.id.btnLogout)

        fabTaskManagement.setOnClickListener {
            val intent = Intent(this, TaskManagementActivity::class.java)
            startActivity(intent)
        }

        fabEventScheduling.setOnClickListener {
            val intent = Intent(this, EventSchedulingActivity::class.java)
            startActivity(intent)
        }

        fabWeatherUpdates.setOnClickListener {
            val intent = Intent(this, WeatherUpdatesActivity::class.java)
            startActivity(intent)
        }

        fabGeneralInfo.setOnClickListener {
            val intent = Intent(this, GeneralInfoActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Clear the user session from SharedPreferences
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
