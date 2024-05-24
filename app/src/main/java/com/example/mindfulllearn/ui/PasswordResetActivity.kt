package com.example.mindfulllearn.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfulllearn.R
import com.example.mindfulllearn.ViewModel.CheckPasswordViewModel
import com.example.mindfulllearn.ViewModel.ChangePasswordViewModel

class PasswordResetActivity : AppCompatActivity() {

    private val checkPasswordViewModel: CheckPasswordViewModel by viewModels()
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()

    // Declare newPasswordEditText as a class-level property
    private lateinit var newPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.passwordresetfromprofile)

        // Find your UI elements
        val oldPasswordEditText: EditText = findViewById(R.id.edittext5)
        newPasswordEditText = findViewById(R.id.editText1) // Assign the value here

        val newPasswordVerifyEditText: EditText = findViewById(R.id.passveri)
        val resetButton: Button = findViewById(R.id.loginButton)


        // Set up click listener for the reset button
        resetButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val newPasswordVerify = newPasswordVerifyEditText.text.toString()

            // Check if the new passwords match
            if (newPassword != newPasswordVerify) {
                // Handle password mismatch
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setContentView(R.layout.loading_screen)
            // Check the old password
            //checkPassword(oldPassword)
        }
    }
/*
    private fun checkPassword(oldPassword: String) {
        // Get the user ID from your preferences or wherever you store it
        val userId = getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
            .getString("userId", "") ?: "" // Replace with your actual way of obtaining user ID
        println(userId);
        // Observe the response from the ViewModel
        checkPasswordViewModel.checkPasswordResponse.observe(this) { response ->
            if (response.isSuccessful) {
                // Old password is correct, proceed to change the password
                changePassword(userId, newPasswordEditText.text.toString())
            } else {
                // Old password is incorrect
                Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        // Call the ViewModel function to check the password
        checkPasswordViewModel.checkPassword(userId, oldPassword)
    }

    private fun changePassword(userId: String, newPassword: String) {
        // Observe the response from the ViewModel
        changePasswordViewModel.changePasswordResponse.observe(this) { response ->
            if (response.isSuccessful) {
                // Password changed successfully
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()

                // Navigate to UserProfileActivity after a successful password change
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Password change failed
                Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show()
            }
        }

        // Call the ViewModel function to change the password
        changePasswordViewModel.changePassword(userId, newPassword)
    }
*/
}
