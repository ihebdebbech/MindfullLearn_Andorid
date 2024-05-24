package com.example.mindfulllearn.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mindfulllearn.R
import com.example.mindfulllearn.data.LoginResponse
import com.example.mindfulllearn.data.SignupResponse
import com.example.mindfulllearn.data.UserProfileResponse
import com.example.mindfulllearn.ViewModel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.User
import com.google.gson.Gson
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    var userAdmin: User? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Find your UI elements
        val emailEditText: EditText = findViewById(R.id.editText1)
        val passwordEditText: EditText = findViewById(R.id.editText2)
        val loginButton: Button = findViewById(R.id.loginButton)

        // Observe the login response
       /* loginViewModel.loginResponse.observe(this, Observer { response ->
            setContentView(R.layout.loading_screen)
            if (response.isSuccessful) {

                val token = response.body()?.token
                println(token)
                println("test for the token retrival")// Assuming your LoginResponse has a 'token' property
                authenticateUserProfile(token)
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setContentView(R.layout.login_page)
                Toast.makeText(this, "Login unsuccessful. User does not exist.", Toast.LENGTH_SHORT).show()
            }
        })*/

        // Set up click listener for the login button
        loginButton.setOnClickListener {

            // Get user input
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            lifecycleScope.launch {

                try {
            // Trigger the login function in the ViewModel
            userAdmin = loginViewModel.loginUser(email, password)
                    val gson = Gson()
                    val jsonString = gson.toJson(userAdmin)
                    Log.e("USERRCONNECTED",jsonString)
                    val editor = preferences.edit()
                    editor.putString("user", jsonString)

                    editor.apply()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    // Handle exceptions
                    e.printStackTrace()
                }
            }
        }
    }
/*
    private fun authenticateUserProfile(token: String?) {
        // Make an API call to authenticate-profile endpoint with the received token
        // Use Retrofit or your preferred HTTP client for this

        // Example using Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        token?.let {
            apiService.authenticateUserProfile(it)
                .enqueue(object : Callback<UserProfileResponse> {
                    override fun onResponse(
                        call: Call<UserProfileResponse>,
                        response: Response<UserProfileResponse>
                    ) {
                        if (response.isSuccessful) {
                            when (response.code()) {
                                404 -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "User not found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                401 -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Invalid password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    val userProfile = response.body()
                                    val userId = userProfile?.id
                                    val email = userProfile?.email
                                    val fullname = userProfile?.fullname
                                    val dateOfBirth = userProfile?.dateOfBirth // Change this based on the actual field name
                                    val role = userProfile?.role
                                    val image = userProfile?.image
                                    val profileBio = userProfile?.profileBio
                                    val location = userProfile?.location
                                    val phoneNumber = userProfile?.phoneNumber
                                    val isActive = userProfile?.isActive ?: false
                                    val isBanned = userProfile?.isBanned ?: false
                                    val isVerified = userProfile?.isVerified
                                    if (!isActive) {
                                        // User is not active, destroy session and redirect to inactive page
                                        preferences.edit().clear().apply()
                                        val intent = Intent(this@LoginActivity, DeactivatedUserActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        return
                                    }

                                    if (isBanned == true) {
                                        // User is banned, destroy session and redirect to banned page
                                        preferences.edit().clear().apply()
                                        val intent = Intent(this@LoginActivity, BannedUserActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        return
                                    }

                                    // Extract other fields as needed
                                    println("UserId: $userId")
                                    println("Email: $email")
                                    println("Fullname: $fullname")
                                    println("Date of Birth: $dateOfBirth")
                                    println("Role: $role")
                                    println("Profile Picture: ${image ?: "N/A"}")
                                    println("Profile Bio: ${profileBio ?: "N/A"}")
                                    println("Location: ${location ?: "N/A"}")
                                    println("Phone Number: ${phoneNumber ?: "N/A"}")
                                    println("Active: ${isActive ?: false}")
                                    println("Banned: ${isBanned ?: false}")
                                    println("Verified: ${isVerified ?: false}")
                                    preferences.edit().clear().apply()
                                    val editor = preferences.edit()
                                    editor.putString("userId", userId)
                                    editor.putString("email", email)
                                    editor.putString("fullname", fullname)
                                    editor.putString("dateOfBirth", dateOfBirth)
                                    editor.putString("role", role)
                                    editor.putString("profilePicture", image)
                                    editor.putString("profileBio", profileBio)
                                    editor.putString("location", location)
                                    editor.putString("phoneNumber", phoneNumber)
                                    editor.putBoolean("isActive", isActive ?: false)
                                    editor.putBoolean("isBanned", isBanned ?: false)
                                    editor.putBoolean("isVerified", isVerified ?: false)
                                    editor.apply()

                                }                                }
                            // Handle successful authentication and extract user profile data


                            // Proceed to the next screen or perform any other actions
                        } else {
                            println("error extracting info from the token for some fucking reason!!!!!!!!!!!!!")
                        }
                    }

                    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                        // Handle network or other errors
                    }
                })
        }

    }
}
interface ApiService {
    @GET("/api/users/authenticate-profile")
    fun authenticateUserProfile(@Header("Authorization") token: String): Call<UserProfileResponse>*/
}


