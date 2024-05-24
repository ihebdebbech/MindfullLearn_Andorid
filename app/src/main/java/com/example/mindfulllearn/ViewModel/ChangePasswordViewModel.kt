package com.example.mindfulllearn.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import retrofit2.Response

class ChangePasswordViewModel : ViewModel() {
/*
    private val _changePasswordResponse = MutableLiveData<Response<ResetPasswordResponse>>()
    val changePasswordResponse: LiveData<Response<ResetPasswordResponse>> get() = _changePasswordResponse

    // Function to change the user's password
    fun changePassword(userId: String, newPassword: String) {
        viewModelScope.launch {
            try {
                // Make the API call to change the password
                val response = RetrofitClient.apiService.resetPassword(userId, newPassword)
                if (response.isSuccessful) {
                    // Access the message and updatedUser properties from ResetPasswordResponse
                    val message = response.body()?.message
                    val updatedUser: User? = response.body()?.updatedUser

                    // You can now use 'message' and 'updatedUser' as needed
                }
                // Update the LiveData with the response
                _changePasswordResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }*/
}
