package com.example.mindfulllearn.ui

import VerifyUserViewModel
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mindfulllearn.R


class PinEntryActivity : AppCompatActivity() {

    private lateinit var pinEditText: EditText
    private lateinit var verifyUserViewModel: VerifyUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_pin)

        // Initialize views
        val resetButton: Button = findViewById(R.id.loginButton)
        pinEditText = findViewById(R.id.editText1)

        // Initialize ViewModel
        verifyUserViewModel = ViewModelProvider(this).get(VerifyUserViewModel::class.java)

        // Set click listener for the "Reset" button
        resetButton.setOnClickListener {
          //  VerifyUser()
        }

        // ... (Your existing code for setting up views)
    }
/*
    private fun VerifyUser() {
        // Retrieve entered PIN
        val pin = pinEditText.text.toString()

        // Validate PIN
        if (TextUtils.isEmpty(pin)) {
            // Show an error message or toast and return
            // For example:
            // Toast.makeText(this, "Please enter the PIN", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve user ID from SharedPreferences
        val userId = getSharedPreferences("your_preference_name", MODE_PRIVATE)
            .getString("userId", "") ?: ""

        // Call the ViewModel function to verify the user
        verifyUserViewModel.verifyUser(userId, pin)

        // Observe the response from the ViewModel
        verifyUserViewModel.verifyUserResponse.observe(this) { response ->
            if (response.isSuccessful) {
                val intent = Intent(this@PinEntryActivity, UserProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Handle the verification failure
                // For example, show an error message
            }
        }
    }

    // ... (Your existing code)
*/
}
