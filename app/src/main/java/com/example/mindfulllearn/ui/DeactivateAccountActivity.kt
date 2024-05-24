package com.example.mindfulllearn.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfulllearn.R
import com.example.mindfulllearn.data.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DeactivateAccountActivity : AppCompatActivity() {
/*
    // Assuming you have the necessary imports
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.areyousure_check) // Replace with your layout file name

        // Find your UI elements
        val passwordEditText: EditText = findViewById(R.id.editText1)
        val deactivateButton: Button = findViewById(R.id.loginButton) // Assuming you are using the same button for deactivation
        val returnToProfileButton: Button = findViewById(R.id.returntopf)

        // Set up click listener for the deactivate button
        deactivateButton.setOnClickListener {
            // Call the function to deactivate the account
            deactivateAccount(passwordEditText.text.toString())
        }
        returnToProfileButton.setOnClickListener {
            // Navigate to the user profile activity
            val intent = Intent(this@DeactivateAccountActivity, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun deactivateAccount(enteredPassword: String) {
        // Get user ID from SharedPreferences
        val userId = preferences.getString("userId", "")
        if (userId.isNullOrBlank()) {
            showToast("User ID not available.")
            return
        }
        // Call the checkPassword API
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.checkPassword(userId, enteredPassword)
                if (response.isSuccessful && response.body()?.message == "Password is correct") {
                    // Password is correct, proceed to deactivate the account
                    deactivateUserAccount(userId)
                } else {
                    // Password is incorrect, show an error message to the user
                    runOnUiThread {
                        showToast("Incorrect password. Please try again.")
                    }
                }
            } catch (e: HttpException) {
                // Handle network or other errors
                runOnUiThread {
                    showToast("Error checking password: ${e.message()}")
                }
            }
        }
    }

    private fun deactivateUserAccount(userId: String?) {
        // Ensure userId is not null or empty
        if (userId.isNullOrBlank()) {
            showToast("User ID not available.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deactivateUser(userId)
                if (response.isSuccessful) {
                    // Account deactivated successfully
                    runOnUiThread {
                        // Clear SharedPreferences
                        preferences.edit().clear().apply()

                        // Redirect to the landing page
                        val intent = Intent(this@DeactivateAccountActivity, LandingPageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Handle unsuccessful deactivation, show an error message
                    runOnUiThread {
                        showToast("Error deactivating account: ${response.message()}")
                    }
                }
            } catch (e: HttpException) {
                // Handle network or other errors
                runOnUiThread {
                    showToast("Error deactivating account: ${e.message()}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

 */
}
