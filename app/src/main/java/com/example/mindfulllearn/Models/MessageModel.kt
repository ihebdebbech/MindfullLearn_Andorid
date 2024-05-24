package com.example.mindfulllearn.Models

data class MessageModel(
     val type: String,
     val name: String? = null,
     val target: String? = null,
     val data:Any?=null
)
