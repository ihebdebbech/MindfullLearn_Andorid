package com.example.mindfulllearn.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindfulllearn.data.ModifyUserResponse
import com.example.mindfulllearn.data.RetrofitClient
import com.example.mindfulllearn.Models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ModifyProfileViewModel : ViewModel() {


    private val client = OkHttpClient()

     suspend fun modifyUser(
      user:User



    ) :User {
        return suspendCoroutine { continuation ->
            val requestBody = FormBody.Builder()
                .add("firstname", user.firstname)
                .add("lastname",user.lastname)
                .add("image", user?.image ?: "0.0")
                .add("role",user.role)
                .add("dateOfBirth",user.dateOfBirth)
                .add("latitude", (user?.latitude ?: 0.0).toString())
                .add("longitude", (user?.longitude ?: 0.0).toString())
                .add("email",user.email)

                .add("password", user.password)

                .build()
            val request = Request.Builder()
                .url("https://mindfullearn-6od2.onrender.com/api/user/update/"+user.__id ?: user._id ?:"0")
                .put(requestBody)
                // Replace with your API endpoint
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string() // Get response data as a string

                        responseData?.let {
                            val user2 = object : TypeToken<User>() {}.type
                            val users: User = Gson().fromJson(it, user2)
                            Log.d("userrrrupdated", users.toString())
                            // Now you have a list of User objects (users), use it for your map

                            continuation.resume(user)
                        }
                    } else {
                        // Handle unsuccessful response
                        continuation.resumeWithException(IOException("Network error"))
                    }
                }
            })
        }
    }

}
