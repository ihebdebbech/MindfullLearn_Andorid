package com.example.mindfulllearn.Models
import java.util.Date
import com.google.gson.annotations.SerializedName
data class User(
    @SerializedName("__id")
    val __id: String?,

    @SerializedName("_id")
    val _id: String?,
    val role: String,
    var firstname: String,
    var lastname: String,
    var email: String,
    val image: String?,
    var dateOfBirth: String,
    val password: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val version: String?,
    val latitude: Double?,
    val longitude: Double?
)

