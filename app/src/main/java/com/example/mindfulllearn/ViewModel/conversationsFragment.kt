package com.example.mindfulllearn.ViewModel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.Models.MessageModel
import com.example.mindfulllearn.R
import com.example.mindfulllearn.databinding.FragmentBlankBinding
import com.example.mindfulllearn.databinding.FragmentConversationsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class conversationsFragment : Fragment(),converstionsadapter.ConversationItemClickListener {
    private val client = OkHttpClient()
    private lateinit var binding: FragmentConversationsBinding
    private var recyclerView: RecyclerView? = null
    private lateinit var mainActivity: MainActivity
    private lateinit var conversationsAdapter: converstionsadapter
    private var messagesList: MutableList<Message> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversationsBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerConversations
        recyclerView!!.layoutManager = LinearLayoutManager(requireContext())
        mainActivity = (activity as? MainActivity)!!
        lifecycleScope.launch {
            try {
                messagesList = getuserconversations().toMutableList()
                conversationsAdapter = converstionsadapter(messagesList, mainActivity,this@conversationsFragment)
                recyclerView!!.adapter = conversationsAdapter
            }
            catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }

        }
    }

    suspend fun getuserconversations(): List<Message> {

        return suspendCoroutine { continuation ->
            // Log.e("errrorr", "iiinnnn")
        //    Log.e("IDDD", mainActivity.userAdmin.toString())
            val requestBody = FormBody.Builder()
                .add("userId", mainActivity.userAdmin!!.__id!!)

                .build()
            val url =
                "https://mindfullearn-6od2.onrender.com/api/conversations/" // Replace with your server URL

            val request = Request.Builder()
                .post(requestBody)
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure
                    e.printStackTrace()
                    continuation.resumeWithException(e)

                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string() // Get response data as a string

                        responseData?.let {
                            val messagesType = object : TypeToken<List<Message>>() {}.type
                            val messagesList1: List<Message> = Gson().fromJson(it, messagesType)

                            // Now you have a list of Message objects (messagesList), use it as needed
                            for (message in messagesList1) {
                                 Log.e("message", message.toString())
                            }
                            continuation.resume(messagesList1)
                        } ?: continuation.resume(emptyList())
                    } else {
                        continuation.resumeWithException(IOException("Network error"))
                    }

                }
            })
        }
    }

    override fun onItemClick(messageInfo: Message) {
        val bundle1 = Bundle()
        val newFragment =BlankFragment()
        newFragment.arguments = bundle1
        if(messageInfo.sender == mainActivity.userAdmin!!.__id) {
            bundle1.putString("markerTitle", messageInfo.recipient)
            bundle1.putString("coachid", messageInfo.recipient)
        }
        else{
            bundle1.putString("markerTitle", messageInfo.sender)
            bundle1.putString("coachid", messageInfo.sender)
        }

        mainActivity?.supportFragmentManager?.beginTransaction()?.apply {
            replace( R.id.fragmentContainerView,newFragment)
            addToBackStack("chat")
            commit()
        }

    }
}