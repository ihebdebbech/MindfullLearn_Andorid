package com.example.mindfulllearn.ViewModel

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.MessagingSocket
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.Models.MessageModel
import com.example.mindfulllearn.R
import com.example.mindfulllearn.databinding.FragmentBlankBinding
import com.example.mindfulllearn.databinding.FragmentGetintouchBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    private lateinit var messagingSocket : MessagingSocket
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentBlankBinding
    private lateinit var coachid: String
    private lateinit var mainActivity: MainActivity
   // private lateinit var messageslist: List<Message>
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private var messagesList: MutableList<Message> = mutableListOf()
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = arguments
        val markerTitle = bundle?.getString("markerTitle")
        coachid = bundle?.getString("coachid").toString()
        // Inflate the layout for this fragment
        binding = FragmentBlankBinding.inflate(inflater, container, false)
        binding.coachname1.text = markerTitle
        binding.videocall1.setOnClickListener {

            // val fragmentManager = parentFragmentManager.findFragmentById(R.id.fragmentContainerView)
              //   recyclerView = view?.findViewById(R.id.recycler_gchat3) ?: RecyclerView(requireContext())
           // getusermessages()
            mainActivity?.socketRepository?.sendMessageToSocket(
                MessageModel(
                "start_call",mainActivity?.userName,coachid,null
            )
            )

            mainActivity?.usernametocall = coachid
           // mainActivity?.call()
            mainActivity?.target=mainActivity?.usernametocall!!
        }
        binding.phonecall.setOnClickListener {
            val newFragment = conversationsFragment()

            mainActivity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainerView, newFragment)
                addToBackStack("conversations")
                commit()
            }
        }
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerGchat3
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mainActivity = (activity as? MainActivity)!!
       messagingSocket = MessagingSocket(mainActivity.userAdmin!!,this)
        lifecycleScope.launch {
            try {
       messagesList= getusermessages().toMutableList()
                messageAdapter = MessageAdapter(messagesList,mainActivity)
                recyclerView.adapter = messageAdapter
                Log.e("teeeeeesssst",messagesList.toString())
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
        binding.buttonGchatSend1.setOnClickListener {
            hideKeyboard(view)
            sendMessage(binding.editGchatMessage1.text.toString())
            binding.editGchatMessage1.text.clear()

        }
    }


    private suspend fun getusermessages(): List<Message> {
        /* fun getMessagesBySenderAndRecipient(senderId: String, recipientId: String) {
             val client = OkHttpClient()

             val url = "http://localhost:3000/messages/$senderId/$recipientId" // Replace with your server URL

             val request = Request.Builder()
                 .url(url)
                 .build()

             client.newCall(request).enqueue(object : Callback {
                 override fun onResponse(call: Call, response: Response) {
                     val responseBody = response.body?.string()
                     // Handle the response here
                     println("Response: $responseBody")
                 }

                 override fun onFailure(call: Call, e: IOException) {
                     println("Request failed: ${e.message}")
                 }
             })
         }*/
        /*val requestBody = FormBody.Builder()
            .add("actifuser", mainActivity.userAdmin.id)
            .add("coach", coachid)
            // Add more parameters if needed
            .build()*/

        return suspendCoroutine { continuation ->
           // Log.e("errrorr", "iiinnnn")
            Log.e("IDDD", mainActivity.userAdmin.toString())
            val url =
                "https://mindfullearn-6od2.onrender.com/api/messages/${mainActivity.userAdmin?.__id.toString()}/${coachid}" // Replace with your server URL

            val request = Request.Builder()
                .url(url)
                .build()

            // Replace with your API endpoint


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
                               // Log.e("message", message.toString())
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
    fun sendMessage( message: String) {
        val jsonObject = JSONObject()

        jsonObject.put("recipient", coachid)
        jsonObject.put("sender", mainActivity.userAdmin!!.__id ?: mainActivity.userAdmin!!._id)
        jsonObject.put("content", message)


        messagingSocket.socket.emit("sendMessage", jsonObject.toString())
       /* val client = OkHttpClient()
        val url =
            "https://mindfullearn-6od2.onrender.com/api/message/" // Replace with your server URL
       //  val mes = Message(null,coachid,mainActivity.userAdmin!!._id,message,null,null);
        val requestBody = FormBody.Builder()
            .add("recipient", coachid)
            .add("sender",mainActivity.userAdmin!!.__id!!)
            .add("content",message)


            .build()

        val request = Request.Builder()
            .url(url) // Replace with your API endpoint
            .post(requestBody)

            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string() // Get response data as a string

                    responseData?.let {
                        val messagesType = object : TypeToken<Message>() {}.type
                        messagesList.add(Gson().fromJson(it, messagesType))
                        activity?.runOnUiThread {
                            messageAdapter.notifyDataSetChanged()
                        }

                    }

                    // Message sent successfully
                } else {
                    // Handle unsuccessful response
                }
            }
        })*/
    }
    fun addMessageToList(message: Message) {
        messagesList.add(message)
        activity?.runOnUiThread {
            messageAdapter.notifyDataSetChanged()

        }
    }
}