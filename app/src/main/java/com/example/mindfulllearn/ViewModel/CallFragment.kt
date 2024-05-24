package com.example.mindfulllearn.ViewModel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.mindfulllearn.MainActivity
import com.example.mindfulllearn.Models.IceCandidateModel
import com.example.mindfulllearn.Models.MessageModel
import com.example.mindfulllearn.R
import com.example.mindfulllearn.RTCClient
import com.example.mindfulllearn.SocketRepository
import com.example.mindfulllearn.databinding.ActivityCallBinding
import com.example.mindfulllearn.databinding.FragmentCallBinding

import com.example.mindfulllearn.utils.NewMessageInterface
import com.example.mindfulllearn.utils.PeerConnectionObserver
import com.example.mindfulllearn.utils.RTCAudioManager
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import org.webrtc.MediaStream

class CallFragment : Fragment(), NewMessageInterface {


    lateinit var binding : FragmentCallBinding
    var messagertc: MessageModel?=null
    private var userName:String?=null
    var imcalling :String?=null
    private var socketRepository: SocketRepository?=null
    private var rtcClient : RTCClient?=null
    private val TAG = "CallActivity"
    private var target:String = ""
    private val gson = Gson()
    private var isMute = false
    private var isCameraPause = false
    lateinit var mainactivity : MainActivity
    private val rtcAudioManager by lazy { RTCAudioManager.create(this.requireContext()) }
    private var isSpeakerMode = true




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        imcalling = bundle?.getString("imcalling")
        mainactivity = (activity as? MainActivity)!!
        // Inflate the layout for this fragment
        binding = FragmentCallBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init(){

        userName = "iheb"

        socketRepository = SocketRepository(this)
        userName?.let { socketRepository?.initSocket(it) }

        rtcClient = RTCClient(mainactivity.application,userName!!,socketRepository!!, object : PeerConnectionObserver() {
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)
                rtcClient?.addIceCandidate(p0)
                val candidate = hashMapOf(
                    "sdpMid" to p0?.sdpMid,
                    "sdpMLineIndex" to p0?.sdpMLineIndex,
                    "sdpCandidate" to p0?.sdp
                )

                socketRepository?.sendMessageToSocket(
                    MessageModel("ice_candidate",userName,target,candidate)
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
            callBtn.setOnClickListener {


                socketRepository?.sendMessageToSocket(MessageModel(
                    "start_call",userName,targetUserNameEt.text.toString(),null
                ))
                target = targetUserNameEt.text.toString()
                setWhoToCallLayoutGone()
                setCallLayoutVisible()
            }

            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute){
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                }else{
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                rtcClient?.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause){
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                }else{
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                rtcClient?.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode){
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                }else{
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }
            endCallButton.setOnClickListener {
                setCallLayoutGone()
                setWhoToCallLayoutVisible()
                setIncomingCallLayoutGone()
                rtcClient?.endCall()
            }
        }

    }

    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG, "onNewMessage: $message")
        when(message.type){
            "call_response"->{
                if (message.data == "user is not online"){
                    //user is not reachable
                    activity?.runOnUiThread {
                        Toast.makeText(this.context,"user is not reachable",Toast.LENGTH_LONG).show()
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            setCallLayoutGone()
                            setWhoToCallLayoutVisible()
                            setIncomingCallLayoutGone()
                            println("Task executed after 10 seconds")
                        }, 10000)
                    }
                }else{
                    //we are ready for call, we started a call
                    activity?.runOnUiThread {
                        setWhoToCallLayoutGone()
                        setCallLayoutVisible()
                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
                            rtcClient?.startLocalVideo(localView)
                            rtcClient?.call("omar")
                        }


                    }

                }
            }
            "answer_received" ->{

                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                activity?.runOnUiThread {
                    binding.remoteViewLoading.visibility = View.GONE
                }
            }
            "offer_received" ->{
                activity?.runOnUiThread {
                    setIncomingCallLayoutVisible()
                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                        setCallLayoutVisible()
                        setWhoToCallLayoutGone()

                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
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


            "ice_candidate"->{
                try {
                    val receivingCandidate = gson.fromJson(gson.toJson(message.data),
                        IceCandidateModel::class.java)
                    rtcClient?.addIceCandidate(IceCandidate(receivingCandidate.sdpMid,
                        Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),receivingCandidate.sdpCandidate))
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun setIncomingCallLayoutGone(){
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

    private fun setWhoToCallLayoutGone() {
        binding.whoToCallLayout.visibility = View.GONE
    }

    private fun setWhoToCallLayoutVisible() {
        binding.whoToCallLayout.visibility = View.VISIBLE
    }


}