package com.example.mindfulllearn.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// YourRetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "https://localhost:3000"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

// ApiService.kt
interface ApiService {
    @FormUrlEncoded
    @POST("/api/user/add") // Add the complete endpoint path
    suspend fun signup(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("image") image: String,
        @Field("role") role: String,
        @Field("password") password: String,

        ): Response<SignupResponse>

    @FormUrlEncoded
    @POST("/api/user/login") // Adjust the endpoint as per your API
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>
    // Add other API endpoints as needed
    @FormUrlEncoded
    @PUT("/api/user/update/{id}") // Add the complete endpoint path
    suspend fun modifyUser(
        @Path("id") userId: String,
        @Field("email") email: String?,
        @Field("firstname") firstname: String?,
        @Field("lastname") lastname: String?,
        @Field("image") image: String?,
        @Field("dateOfbirth") dateOfbirth: String?,
        @Field("role") role: String?,
    ): Response<ModifyUserResponse>
    @FormUrlEncoded
    @POST("/api/users/{id}/checkpass") // Add the complete endpoint path
    suspend fun checkPassword(
        @Path("id") userId: String,
        @Field("password") password: String
    ): Response<CheckPasswordResponse>
    @FormUrlEncoded
    @PUT("/api/users/users/{id}/deactivate") // Add the complete endpoint path
    suspend fun deactivateUser(
        @Path("id") userId: String,
        @Field("dummyField") dummyField: String = "" // Add a dummy field as @Field annotation requires at least one field
    ): Response<DeactivateUserResponse>


    @FormUrlEncoded
    @POST("/api/user/password")
    suspend fun resetPassword(
        @Path("email") email: String,
        @Path("newPassword") password: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<ResetPasswordResponse>
    @FormUrlEncoded
    @POST("/api/user/forgotpassword/sendcode")
    suspend fun generateVerificationToken(
        @Path("id") userId: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<GenerateTokenResponse>
    @FormUrlEncoded
    @POST("/api/verify/verify/{id}/{pin}")
    suspend fun verifyUser(
        @Path("id") userId: String,
        @Path("pin") pin: String,
        @Field("dummyField") dummyField: String = ""
    ): Response<VerifyUserResponse>
}
