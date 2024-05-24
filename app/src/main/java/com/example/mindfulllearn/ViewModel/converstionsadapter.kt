package com.example.mindfulllearn.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.R

class converstionsadapter(private val messages: List<Message>,private val mainActivity: MainActivity,private val listener: ConversationItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface ConversationItemClickListener {
        fun onItemClick(messageInfo: Message) // Define your message info model
    }



    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_conversation_message, parent, false)
          return conversationsViewHolder(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

                val conversationsHolder = holder as conversationsViewHolder
                conversationsHolder.bind(message)



    }



    override fun getItemCount(): Int {

        return messages.size

    }


    inner class conversationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContent: TextView = itemView.findViewById(R.id.message_content)
        private val user_name: TextView = itemView.findViewById(R.id.user_name)
        private val coachId: TextView = itemView.findViewById(R.id.userId)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val messageInfo = // Get message info from your dataset at the position
                        listener.onItemClick(messages[position])
                }
            }
        }
        fun bind(message: Message) {
            Log.e("bind", "inn")

            messageContent.text = message.content
            if(message.sender == mainActivity.userAdmin!!.__id) {
                user_name.text = message.recipient
                coachId.text = message.recipient
            }
            else{
                user_name.text = message.sender
                coachId.text = message.sender
            }
            // Bind other message attributes to respective views
        }
    }
}