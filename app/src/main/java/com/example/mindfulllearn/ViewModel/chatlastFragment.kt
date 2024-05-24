package com.example.mindfulllearn.ViewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.Message
import com.example.mindfulllearn.R

import com.example.mindfulllearn.databinding.FragmentChatlastBinding
import okhttp3.OkHttpClient

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [chatlastFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class chatlastFragment : Fragment() {
    private lateinit var binding : FragmentChatlastBinding
    private lateinit var coachid:String
    private lateinit var mainActivity: MainActivity
    private lateinit var messageslist : List<Message>
    private val client = OkHttpClient()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        val markerTitle = bundle?.getString("markerTitle")
        coachid = bundle?.getString("coachid").toString()
        // Inflate the layout for this fragment
        binding = FragmentChatlastBinding.inflate(inflater, container, false)
        binding.coachname1.text=markerTitle
        //getusermessages()
        // messageAdapter = MessageAdapter(messagesList)
        // recyclerView.adapter = messageAdapter

        return view
        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videocall1.setOnClickListener{
             mainActivity = (activity as? MainActivity)!!
            // val fragmentManager = parentFragmentManager.findFragmentById(R.id.fragmentContainerView)
            mainActivity?.usernametocall= binding.coachname1.text.toString()
           mainActivity?.call()
            }
        }*/
    /*
        private fun getusermessages() {
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
            val url = "http://172.20.10.5:3000/api/messages/655eb05c047452171c7bc86f/6560221a99baaa01201fe766" // Replace with your server URL

            val request = Request.Builder()
                .url(url)
                .build()

                // Replace with your API endpoint


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string() // Get response data as a string

                        responseData?.let {
                            val messagesType = object : TypeToken<List<Message>>() {}.type
                            val messagesList: List<Message> = Gson().fromJson(it, messagesType)

                            // Now you have a list of Message objects (messagesList), use it as needed
                            for (message in messagesList) {
                                Log.e("message", message.toString())
                            }
                        }
                    } else {
                        // Handle unsuccessful response
                    }
                }
            })
        }*/
}

