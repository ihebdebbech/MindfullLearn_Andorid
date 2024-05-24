package com.example.mindfulllearn.data

import com.example.mindfulllearn.Models.User

class ResetPasswordResponse(updatedUser: User, message: String) {
    val updatedUser: User = updatedUser
    val message: String = message
}
