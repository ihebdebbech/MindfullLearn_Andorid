package com.example.mindfulllearn.Models

import java.util.Date

data class Message(
    val _id: String?,
    val recipient: String,
    val sender: String,
    val content: String,
    val timestamp: Date?,
    val __v: Int?
)
