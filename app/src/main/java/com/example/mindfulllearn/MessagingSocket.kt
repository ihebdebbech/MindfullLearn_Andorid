package com.example.mindfulllearn

import android.util.Log
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.Models.User
import com.example.mindfulllearn.ViewModel.BlankFragment
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MessagingSocket(user: User, private val fragment: BlankFragment) {

 val socket: Socket = IO.socket("https://mindfullearn-6od2.onrender.com/") // Replace with your Node.js server URL

    init {
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketMessages","Connected to  backend")
            socket.emit("userConnected", user._id ?: user.__id)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketMessages","Disconnected from  backend")
        }

        socket.on("receiveMessage") { args ->
            if (args.isNotEmpty()) {
                val receivedString = args[0] as String

                try {
                    val receivedMessage = JSONObject(receivedString)

                val message = Message(
                    "0",

                    recipient = receivedMessage.getString("recipient"),
                    sender = receivedMessage.getString("sender"),
                    content = receivedMessage.getString("content"),
                   timestamp =  receivedMessage.optLong("timestamp")?.let { Date(it) },
                    0
                    )
                // Handle the received message as needed
                Log.d("Received message: ",message.toString())
                    fragment.addMessageToList(message)
                } catch (e: JSONException) {
                    Log.e("JSON Parsing Error: ", e.toString())
                }
            }
        }

        socket.connect()
    }


}