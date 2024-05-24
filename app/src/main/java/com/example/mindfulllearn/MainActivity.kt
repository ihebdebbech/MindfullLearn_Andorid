package com.example.mindfulllearn

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.mindfulllearn.Models.IceCandidateModel
import com.example.mindfulllearn.Models.MessageModel
import com.example.mindfulllearn.Models.User
import com.example.mindfulllearn.ViewModel.CallFragment
import com.example.mindfulllearn.ViewModel.MessageAdapter
import com.example.mindfulllearn.databinding.ActivityMainBinding
import com.example.mindfulllearn.utils.NewMessageInterface
import com.example.mindfulllearn.utils.PeerConnectionObserver
import com.example.mindfulllearn.utils.RTCAudioManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(),NewMessageInterface {
    private val client = OkHttpClient()
    private var localviewinit = false
    var userName: String = "omar"
    private lateinit var navController: NavController
    var messagertc: MessageModel? = null
    var usernametocall = ""
    private lateinit var binding: ActivityMainBinding
    var userAdmin: User? = null
    var socketRepository: SocketRepository? = null
    private var rtcClient: RTCClient? = null
    private val TAG = "CallActivity"
    var target: String = ""
    private val gson = Gson()
    private var isMute = false
    private var isCameraPause = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }
    private var isSpeakerMode = true
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setupWithNavController(bottomNavigationView ,navController)


            // Inflate the appropriate menu into the Toolbar


        lifecycleScope.launch {


            try {
                userName = getUserAdmin()
                init()

            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
        //socketRepository = SocketRepository(this)
        // userName?.let { socketRepository?.initSocket(it) }
        //val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        // setSupportActionBar(binding.toolbar1)


    }

    private fun replacefragement(fragement: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmenttrans = fragmentManager.beginTransaction()
        fragmenttrans.replace(R.id.fragmentContainerView, fragement)
        fragmenttrans.addToBackStack("incall")
        fragmenttrans.commit()

    }

    private fun init() {

        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) }
        rtcClient = RTCClient(
            application,
            userName!!,
            socketRepository!!,
            object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    rtcClient?.addIceCandidate(p0)
                    val candidate = hashMapOf(
                        "sdpMid" to p0?.sdpMid,
                        "sdpMLineIndex" to p0?.sdpMLineIndex,
                        "sdpCandidate" to p0?.sdp
                    )

                    socketRepository?.sendMessageToSocket(
                        MessageModel("ice_candidate", userName, target, candidate)
                    )

                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    p0?.videoTracks?.get(0)?.addSink(binding.remoteView)
                    Log.d(TAG, "onAddStream: $p0")

                }
            })
        rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)


        binding.apply {

            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute) {
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                } else {
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                rtcClient?.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause) {
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                } else {
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                rtcClient?.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode) {
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                } else {
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }
            endCallButton.setOnClickListener {
                setCallLayoutGone()

                binding.fragmentContainerView.visibility = View.VISIBLE
                setIncomingCallLayoutGone()
             //   rtcClient?.endCall()

              //  localView.clearImage()
              //  remoteView.clearImage()
            }
        }

    }

    public fun call() {
        runOnUiThread {
            binding.apply {
                socketRepository?.sendMessageToSocket(
                    MessageModel(
                        "start_call", userName, usernametocall, null
                    )
                )
                target = usernametocall

                setCallLayoutVisible()
                binding.fragmentContainerView.visibility = View.GONE
            }
        }
    }

    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG, "onNewMessage: $message")
        when (message.type) {
            "call_response" -> {
                if (message.data == "user is not online") {
                    //user is not reachable
                    runOnUiThread {
                        Toast.makeText(this, "user is not reachable", Toast.LENGTH_LONG).show()
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            setCallLayoutGone()

                            setIncomingCallLayoutGone()
                            binding.fragmentContainerView.visibility = View.VISIBLE
                            println("Task executed after 10 seconds")
                        }, 10000)
                    }
                } else {
                    //we are ready for call, we started a call
                    runOnUiThread {

                        setCallLayoutVisible()
                        binding.fragmentContainerView.visibility = View.GONE
                        binding.apply {
                            if(!localviewinit) {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                                localviewinit = true
                            }
                            rtcClient?.startLocalVideo(localView)
                            rtcClient?.call(usernametocall)

                        }


                    }

                }
            }

            "answer_received" -> {

                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                runOnUiThread {
                    binding.remoteViewLoading.visibility = View.GONE
                }
            }

            "offer_received" -> {
                runOnUiThread {
                    setIncomingCallLayoutVisible()

                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                        setCallLayoutVisible()
                        binding.fragmentContainerView.visibility = View.GONE

                        binding.apply {
                            if(!localviewinit) {
                                rtcClient?.initializeSurfaceView(localView)
                                rtcClient?.initializeSurfaceView(remoteView)
                                localviewinit = true
                            }
                            rtcClient?.startLocalVideo(localView)
                        }
                        val session = SessionDescription(
                            SessionDescription.Type.OFFER,
                            message.data.toString()
                        )
                        rtcClient?.onRemoteSessionReceived(session)
                        rtcClient?.answer(message.name!!)
                        target = message.name!!
                        binding.remoteViewLoading.visibility = View.GONE

                    }
                    binding.rejectButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                    }

                }

            }


            "ice_candidate" -> {
                try {
                    val receivingCandidate = gson.fromJson(
                        gson.toJson(message.data),
                        IceCandidateModel::class.java
                    )
                    rtcClient?.addIceCandidate(
                        IceCandidate(
                            receivingCandidate.sdpMid,
                            Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),
                            receivingCandidate.sdpCandidate
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setIncomingCallLayoutGone() {
        binding.incomingCallLayout.visibility = View.GONE
    }

    private fun setIncomingCallLayoutVisible() {
        binding.incomingCallLayout.visibility = View.VISIBLE
    }

    private fun setCallLayoutGone() {
        binding.callLayout.visibility = View.GONE
    }

    private fun setCallLayoutVisible() {
        binding.callLayout.visibility = View.VISIBLE
    }

    private suspend fun getUserAdmin(): String {
        return suspendCoroutine { continuation ->


            val storedUserJson = preferences.getString("user", "")

            val gson = Gson()
            val user = gson.fromJson(storedUserJson, User::class.java)
            userAdmin = user
            Log.e("USERRCONNECTED",userAdmin.toString())
            continuation.resume(userAdmin?.__id!!)

        }
    }
}

