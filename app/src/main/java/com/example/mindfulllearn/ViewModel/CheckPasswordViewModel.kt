package com.example.mindfulllearn.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import retrofit2.Response

class CheckPasswordViewModel : ViewModel() {
/*
    private val _checkPasswordResponse = MutableLiveData<Response<CheckPasswordResponse>>()
    val checkPasswordResponse: LiveData<Response<CheckPasswordResponse>> get() = _checkPasswordResponse

    // Function to check if the entered password is correct
    fun checkPassword(userId: String, password: String) {
        viewModelScope.launch {
            try {
                // Make the API call to check the password
                val response = RetrofitClient.apiService.checkPassword(userId, password)

                // Update the LiveData with the response
                _checkPasswordResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }*/
}
