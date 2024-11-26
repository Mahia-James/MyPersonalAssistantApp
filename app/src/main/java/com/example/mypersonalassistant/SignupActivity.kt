package com.example.mypersonalassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val etName: EditText = findViewById(R.id.etName)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        val btnLogin: Button = findViewById(R.id.btnLogin) // New login button

        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Save user info locally (SharedPreferences)
                val sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("name", name)
                editor.putString("email", email)
                editor.putString("password", password)
                editor.apply()

                // Show success message and go to login activity
                showToast("Sign-up successful! Please log in.")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // Show error message for empty fields
                showToast("Please fill all fields")
            }
        }

        btnLogin.setOnClickListener {
            // Navigate to the LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
