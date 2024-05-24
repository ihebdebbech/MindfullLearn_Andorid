package com.example.mindfulllearn.ui
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.User
import com.example.mindfulllearn.R
import com.example.mindfulllearn.ViewModel.SignupViewModel
import com.google.gson.Gson

import kotlinx.coroutines.launch
import java.time.LocalDate


class SignupActivity : AppCompatActivity() {
    var userAdmin: User? = null
    private val signupViewModel: SignupViewModel by viewModels()
   // val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Find your UI elements
        val firstnameEditText: EditText = findViewById(R.id.firstnametext)
        val lastnameEditText: EditText = findViewById(R.id.lastnametext)
        val emailEditText: EditText = findViewById(R.id.emailtext)
        val passwordEditText: EditText = findViewById(R.id.Password)
        val dateOfBirthEditText: EditText = findViewById(R.id.dateofbirthtext)
        val roleEditText: Spinner = findViewById(R.id.spinnerUserType)
        //val selectedRole: String = roleEditText.selectedItem.toString() // Assuming the spinner is used for role selection
        val signupButton: Button = findViewById(R.id.loginButton) // Assuming the loginButton is used for signup

        // Observe the signup response
       /* signupViewModel.signupResponse.observe(this) { response ->
            // Handle the response, update UI accordingly
            setContentView(R.layout.loading_screen)
            if (response.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setContentView(R.layout.register_page)
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }*/

        // Set up click listener for the signup button
        signupButton.setOnClickListener {
            setContentView(R.layout.loading_screen)
            val firstname = firstnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val lastname = lastnameEditText.text.toString()
            //val dateOfBirth = "$year-$month-$day"
            val role = roleEditText.selectedItem.toString() // Assuming you want the selected item from the spinner
            if (!isValidEmail(email)) {
                // Notify the user that the email format is invalid
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Validate alphabetical characters in the full name
           else if (!isAlphabetical(firstname)) {
                // Notify the user that the full name should be strictly alphabetical
                Toast.makeText(this, "Firstname should be strictly alphabetical", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Validate minimum password length
           else if (password.length < 6) {
                // Notify the user that the password should be at least 8 characters long
                Toast.makeText(this, "Password should be at least 8 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


          else {
                val activityContext = this
                lifecycleScope.launch {

                    try {
                // Notify the user that passwords do not match
                userAdmin= signupViewModel.signupUser(firstname,lastname, email, password,
                    LocalDate.now().toString(), role)

                        val intent = Intent(activityContext, LoginActivity::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        // Handle exceptions
                        e.printStackTrace()
                    }
                }
            }

        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to check if the string is strictly alphabetical
    private fun isAlphabetical(str: String): Boolean {
        return str.matches("[a-zA-Z]+".toRegex())
    }
}

