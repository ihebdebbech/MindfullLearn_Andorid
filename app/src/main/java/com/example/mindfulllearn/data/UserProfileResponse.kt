package com.example.mindfulllearn.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserProfileResponse(
    @SerializedName("_id")  val _id: String,
    @SerializedName("role") val role: String,
    @SerializedName("firstname")  val firstname: String,
    @SerializedName("lastname")  val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("image") val image: String,
    @SerializedName("dateOfbirth") val dateOfBirth: String,
    @SerializedName("password")  val password: String,
    @SerializedName("createdAt")  val createdAt: Date?,
    @SerializedName("updatedAt")  val updatedAt: Date?,
    @SerializedName("__v")  val version: String?,
    @SerializedName("latitude")  val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?
)
