package com.example.mindfulllearn.ui

import GenerateTokenViewModel
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mindfulllearn.R
import com.example.mindfulllearn.Models.User
//import com.example.mindfulllearn.viewmodel.ModifyProfileViewModel
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
class ModifyProfileActivity : AppCompatActivity() {
/*
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
    }

    private lateinit var emailEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var datePicker: DatePicker
    private lateinit var spinnerUserType: Spinner
    private lateinit var locationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modifier_page)

        // Initialize your views
        emailEditText = findViewById(R.id.editText1)
        fullNameEditText = findViewById(R.id.editText5)
        bioEditText = findViewById(R.id.biomodif)
        phoneNumberEditText = findViewById(R.id.phonenumber)
        datePicker = findViewById(R.id.datePicker)
        spinnerUserType = findViewById(R.id.spinnerUserType)
        locationEditText = findViewById(R.id.location) // Updated ID for location EditText
        val returnToProfileButton: Button = findViewById(R.id.returntoprofile)
        // Retrieve SharedPreferences values
        val email = preferences.getString("email", "")
        val fullName = preferences.getString("fullname", "")
        val dateOfBirth = preferences.getString("dateOfBirth", "") // Assuming dateOfBirth is stored as a string
        val userType = preferences.getString("role", "")
        val location = preferences.getString("location", "")
        val bio = preferences.getString("profileBio", "")
        // Set retrieved values to corresponding views
        emailEditText.setText(email)
        fullNameEditText.setText(fullName)
        locationEditText.setText(location)
        bioEditText.setText(bio)
        // Set an initial date on the DatePicker
        if (dateOfBirth != null && dateOfBirth.isNotEmpty()) {
            val dateParts = dateOfBirth.substringBefore("T").split("-")
            val year = dateParts[0].toInt()
            val month = dateParts[1].toInt() - 1 // Month is 0-based
            val day = dateParts[2].toInt()
            datePicker.init(year, month, day, null)
        }

        // Set an initial selection for the spinner
        if (userType != null && userType.isNotEmpty()) {
            val position = when (userType) {
                "Contributeur" -> 0
                "Organisateur" -> 1

                else -> 0 // Default to the first item
            }
            spinnerUserType.setSelection(position)
        }
        //print(email);
        // Set click listener for the modify button
        val modifyButton: Button = findViewById(R.id.modifierbutton)
        modifyButton.setOnClickListener {
            println("You clicked on me!!");
          //  modifyUser()
        }
        val deactivateButton: Button = findViewById(R.id.deactivate)
        deactivateButton.setOnClickListener {
            // Create an intent to navigate to DeactivateAccountActivity
           // val intent = Intent(this, DeactivateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
        returnToProfileButton.setOnClickListener {
            // Navigate to the user profile activity
            val intent = Intent(this@ModifyProfileActivity, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
        val changePasswordButton: Button = findViewById(R.id.changepassword) // Added this line
        changePasswordButton.setOnClickListener {
            // Create an intent to navigate to PasswordResetActivity
            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)
            finish()
        }
        val verifyAccountButton: Button = findViewById(R.id.verifyaccount)
        verifyAccountButton.setOnClickListener {
            // Generate and send verification token
            generateVerificationToken()

            // Navigate to the PIN entry page
            val intent = Intent(this@ModifyProfileActivity, PinEntryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun generateVerificationToken() {
    /*    val userId = preferences.getString("userId", "")
        val generateTokenViewModel =
            ViewModelProvider(this).get(GenerateTokenViewModel::class.java)

        // Call the ViewModel function to generate a verification token
        generateTokenViewModel.generateVerificationToken(userId ?: "")

        // Observe the response from the ViewModel (you can handle the response if needed)
        generateTokenViewModel.generateTokenResponse.observe(this) { response: Response<*> ->
            if (response.isSuccessful) {
                Toast.makeText(
                    this@ModifyProfileActivity,
                    "Verification token generated and sent successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Handle unsuccessful response
                Toast.makeText(
                    this@ModifyProfileActivity,
                    "Failed to generate verification token",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun modifyUser() {
        // Retrieve data from views 
        val email = emailEditText.text.toString().takeIf { it.isNotBlank() }
        val fullName = fullNameEditText.text.toString().takeIf { it.isNotBlank() }
        val bio = bioEditText.text.toString().takeIf { it.isNotBlank() }
        val phoneNumber = phoneNumberEditText.text.toString().takeIf { it.isNotBlank() }
        val dateOfBirth = "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}T00:00:00.000+00:00"
        val role = spinnerUserType.selectedItem?.toString().takeIf { it?.isNotBlank() == true }
        val location = locationEditText.text.toString().takeIf { it.isNotBlank() }
        println(email)
        val id = preferences.getString("userId", "");
        //String id
        println(id)
        // Validate data if needed
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullName)) {
            // Show an error message or toast and return
            Toast.makeText(
                this,
                "Please fill in all required fields such as the full name and email",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Get the ViewModel
        val modifyProfileViewModel = ViewModelProvider(this).get(ModifyProfileViewModel::class.java)

        // Call the ViewModel function to update the user
        modifyProfileViewModel.modifyUser(
            _id = preferences.getString("_id", "") ?: "",
            email = email,

            lastname = lastname,
            firstname = firstname,


        )

        // Observe the response from the ViewModel
        modifyProfileViewModel.modifyUserResponse.observe(this) { response ->
            if (response.isSuccessful) {
                // Handle the modified user object as needed
                val modifiedUser: User? = response.body()?.user
                if (modifiedUser != null) {
                    val modifiedUser: User? = response.body()?.user
                    if (modifiedUser != null) {
                        // Update SharedPreferences with the modified user details
                        val editor = preferences.edit()

                        editor.putString("email", modifiedUser.email)
                        editor.putString("firstname", modifiedUser.firstname)
                        editor.putString("lastname", modifiedUser.lastname)
                        editor.putString("image", modifiedUser.image)
                        editor.putString("password", modifiedUser.password)
                        editor.putString("_id", modifiedUser._id)
                        editor.putString("__v", modifiedUser.version)
                        modifiedUser.longitude?.let { editor.putFloat("longitude", it.toFloat()!!) }
                        modifiedUser.latitude?.let { editor.putFloat("latitude", it.toFloat()!!) }

                       // val formatter = DateTimeFormatter.ISO_DATE_TIME
                       // val parsedDate = LocalDateTime.parse(modifiedUser.dateOfBirth, formatter)
                       // val formattedDate = parsedDate.format(formatter)
                        editor.putString("dateOfBirth",modifiedUser.dateOfBirth)
                        editor.putString("role", modifiedUser.role)
                       // editor.putString("profilePicture", modifiedUser.profilepicture)
                       // editor.putString("profileBio", modifiedUser.profilebio)
                       // editor.putString("location", modifiedUser.location)
                       // editor.putString("phoneNumber", modifiedUser.phonenumber)
                       // editor.putBoolean("isActive", modifiedUser.isActive ?: false)
                       // editor.putBoolean("isBanned", modifiedUser.isBanned ?: false)
                       // editor.putBoolean("isVerified", modifiedUser.isVerified ?: false)
                        editor.apply()

                        // Redirect the user to their profile
                        val intent = Intent(this, UserProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle null modified user
                        Toast.makeText(
                            this@ModifyProfileActivity,
                            "Failed to get modified user data",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
*/
    }
*/
}
