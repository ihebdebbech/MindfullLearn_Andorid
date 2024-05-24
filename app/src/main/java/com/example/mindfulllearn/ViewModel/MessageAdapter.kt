package com.example.mindfulllearn.ViewModel

import android.app.PendingIntent.getActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.R

class MessageAdapter(private val messages: List<Message>,private val mainActivity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == SENDER_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sender_item_message, parent, false)
            MessageViewHolder(view)

        } else {
            Log.e("RECIPIENT","recipiennnt")
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
            RecipientViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            SENDER_VIEW_TYPE -> {
                val senderHolder = holder as MessageViewHolder
                senderHolder.bind(message)
            }
            RECIPIENT_VIEW_TYPE -> {
                Log.e("RECIPIENT","recipiennnt")
                val recipientHolder = holder as RecipientViewHolder
                recipientHolder.bind(message)
            }
        }
    }



    override fun getItemCount(): Int {
        return messages.size
    }
    override fun getItemViewType(position: Int): Int {
        // Determine the view type based on the sender/recipient condition
        return if (messages[position].sender == mainActivity.userAdmin?.__id) {
            SENDER_VIEW_TYPE
        } else {
            RECIPIENT_VIEW_TYPE
        }
    }
    companion object {
        private const val SENDER_VIEW_TYPE = 0
        private const val RECIPIENT_VIEW_TYPE = 1
    }
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContent: TextView = itemView.findViewById(R.id.text_message_sender)

        fun bind(message: Message) {
            Log.e("bind", "inn")
            messageContent.text = message.content
            // Bind other message attributes to respective views
        }
    }
    inner class RecipientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
             private val messageContent: TextView = itemView.findViewById(R.id.text_message_content)

             fun bind(message: Message) {
                 Log.e("bind","inn")
                 messageContent.text = message.content
                 // Bind other message attributes to respective views
             }

    }
}