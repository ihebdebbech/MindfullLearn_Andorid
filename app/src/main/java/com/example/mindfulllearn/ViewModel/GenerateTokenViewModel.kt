// GenerateTokenViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import retrofit2.Response

class GenerateTokenViewModel : ViewModel() {
/*
    private val _generateTokenResponse = MutableLiveData<Response<GenerateTokenResponse>>()
    val generateTokenResponse: LiveData<Response<GenerateTokenResponse>> get() = _generateTokenResponse

    fun generateVerificationToken(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.generateVerificationToken(userId)
                _generateTokenResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }*/
}
