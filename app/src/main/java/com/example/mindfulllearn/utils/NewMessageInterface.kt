package com.example.mindfulllearn.utils

import com.example.mindfulllearn.Models.MessageModel

interface NewMessageInterface {
    fun onNewMessage(message: MessageModel)
}