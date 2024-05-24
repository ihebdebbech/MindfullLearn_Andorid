// VerifyUserViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import retrofit2.Response

class VerifyUserViewModel : ViewModel() {
/*
    private val _verifyUserResponse = MutableLiveData<Response<VerifyUserResponse>>()
    val verifyUserResponse: LiveData<Response<VerifyUserResponse>> get() = _verifyUserResponse

    suspend fun verifyUser(userId: String, pin: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.verifyUser(userId, pin)
                if (response.isSuccessful) {
                    // Access the message and updatedUser properties from ResetPasswordResponse
                    val message = response.body()?.message


                    // You can now use 'message' and 'updatedUser' as needed
                }
                _verifyUserResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }*/
}
