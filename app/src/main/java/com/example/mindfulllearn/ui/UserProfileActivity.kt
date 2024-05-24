package com.example.mindfulllearn.ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfulllearn.R
import android.content.Intent
import android.view.View

class UserProfileActivity : AppCompatActivity() {
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
    }
    private lateinit var username: TextView
    private lateinit var roleTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var bio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)
        username = findViewById(R.id.username)
        roleTextView = findViewById(R.id.role)
        locationTextView = findViewById(R.id.locationTextView)
        bio = findViewById(R.id.bio)

        // Retrieve user information from Intent extras
        val userId = preferences.getString("userId", "")
        val email = preferences.getString("email", "")
        val fullname = preferences.getString("fullname", "")
        val role = preferences.getString("role", "")
        val location = preferences.getString("location", "")
        val profileBio = preferences.getString("profileBio", "")
        val isVerified = preferences.getBoolean("isVerified", true)

// Get reference to the ImageView
        //val verificationTickImageView: ImageView = findViewById(R.id.verificationTick)

// Set visibility based on verification status
      /*  if (isVerified) {
            verificationTickImageView.visibility = View.VISIBLE
        } else {
            verificationTickImageView.visibility = View.GONE
        } */
        // Update UI with user information
        username.text = fullname
        roleTextView.text = role

        // Set default text for location if it's empty
        locationTextView.text = if (TextUtils.isEmpty(location)) {
            "Anonymous."
        } else {
            location
        }

        // Set default text for profile bio if it's empty
        bio.text = if (TextUtils.isEmpty(profileBio)) {
            "This user has not set any profile bio."
        } else {
            profileBio
        }
        val logoutButton: Button = findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            // Clear SharedPreferences
            preferences.edit().clear().apply()

            // Redirect to the landing page
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
            finish()
        }
        val modifyprofile: Button = findViewById(R.id.modifierprofile)
        modifyprofile.setOnClickListener {
            // Clear SharedPreferences

print("hello")
            // Redirect to the landing page
            val intent = Intent(this, ModifyProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        // ... update other UI elements
    }
}