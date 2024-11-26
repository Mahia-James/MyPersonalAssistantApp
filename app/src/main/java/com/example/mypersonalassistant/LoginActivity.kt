package com.example.mypersonalassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user details exist in SharedPreferences
        val sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", null)
        val savedPassword = sharedPref.getString("password", null)

        // If user is already logged in, redirect to MainActivity
        val loggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (loggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etLoginEmail: EditText = findViewById(R.id.etLoginEmail)
        val etLoginPassword: EditText = findViewById(R.id.etLoginPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnCreateAccount: Button = findViewById(R.id.btnCreateAccount) // Added Create Account button

        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()

            // Verify the entered credentials
            if (email == savedEmail && password == savedPassword) {
                // Successful login, set isLoggedIn flag to true
                val editor = sharedPref.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                // Redirect to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userName", sharedPref.getString("name", "User"))
                startActivity(intent)
                finish()
            } else {
                // Show error message if credentials are invalid
                showToast(getString(R.string.invalid_credentials))
            }
        }

        btnCreateAccount.setOnClickListener {
            // Redirect to SignupActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
