package com.example.mindfulllearn


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import com.example.mindfulllearn.Models.User

import com.example.mindfulllearn.ViewModel.getintouchFragment
import com.example.mindfulllearn.databinding.FragmentMapsBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapsFragment : Fragment(R.layout.fragment_maps) {

    // Replace the current fragment with your desired fragment
    private lateinit var client : OkHttpClient
    private lateinit var binding: FragmentMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var map: MapView
    private lateinit var mapController: IMapController
    private lateinit var marker: Marker
    private lateinit var secondMarker: Marker
    private lateinit var path1: Polyline
    private lateinit var startPoint: GeoPoint
    private var lastLocation: Location? = null
    private var requestingLocationUpdates = false
    // private lateinit var webSocket: WebSocket


    companion object {
        const val REQUEST_CHECK_SETTINGS = 20202
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        client = OkHttpClient()
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        mapController = map.controller
        marker = Marker(map)
        secondMarker = Marker(map)
        path1 = Polyline()
        //connectToWebSocket()
        initLocationRequest()

        initLocationCallback()
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        Configuration.getInstance().load(
            requireContext(),
            requireActivity().getPreferences(Context.MODE_PRIVATE)
        )

        val appPerms = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,


            android.Manifest.permission.INTERNET
        )
        getTuteurs()
        requestPermissions.launch(appPerms)
        binding.backstackButton.setOnClickListener {
            // Handle backstack button click action (if needed)
            // For example: Pop the fragment backstack here
            while (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager?.popBackStackImmediate()
                binding.backstackButton.visibility = View.GONE
            }
        }
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                initCheckLocationSettings()
            } else {
                // Handle the case when permissions are not granted
                // Show an error message or request permissions again
            }
        }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (requestingLocationUpdates) {
            requestingLocationUpdates = false
            stopLocationUpdates()
        }
        binding.map.onPause()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("MissingPermission")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsg(status: MyEventLocationSettingsChange) {
        if (status.on) {

            initMap()
        } else {
            Timber.i("Stop something")
        }
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            smallestDisplacement = 10f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }
    }

    private fun initLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let { result ->
                    for (location in result.locations) {
                        updateLocation(location)
                    }
                    Log.e("tag", "location changed")

                }
            }
        }
    }

    private fun initCheckLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        //Log.e("tag","location changed2")
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Timber.d("Settings Location IS OK")
            EventBus.getDefault().post(MyEventLocationSettingsChange(true))
            //  Log.e("tag","location changed 3")
            initMap()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.d("Settings Location sendEx??")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("Settings onActivityResult for $requestCode result $resultCode")
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                initMap()
            }
        }
    }

    private fun initLoaction() { //call in create
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        readLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() { //onResume
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    @SuppressLint("MissingPermission") //permission are checked before
    fun readLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { updateLocation(it) }

            }
    }


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateLocation(newLocation: Location) {
        lastLocation = newLocation
        startPoint = GeoPoint(newLocation.latitude, newLocation.longitude)
        mapController.setCenter(startPoint)
        //  webSocket.send(newLocation.latitude.toString()+":"+newLocation.longitude)

        setPrimaryPositionMarker(startPoint)
        val latitude = 36.8873988
        val longitude = 10.191145
        //setSecondaryPositionMarker(GeoPoint(latitude, longitude))
        map.invalidate()
    }

    private fun initMap() {
        initLoaction()
        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true
            startLocationUpdates()
        }
        mapController.setZoom(13.5)
        map.invalidate()
    }


    data class MyEventLocationSettingsChange(val on: Boolean)

    private fun setPrimaryPositionMarker(geoPoint: GeoPoint) { //Singelton

        marker.title = "Here I am"
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_place_24);

        marker.position = geoPoint
        marker.setOnMarkerClickListener { marker, map ->
            // Show the info fragment
            val bundle = Bundle()
            val existingFragment = childFragmentManager.findFragmentByTag("getintouchFragment")

            Log.e("marker name", marker.title)

            bundle.putString("markerTitle", marker.title)
            // If no existing fragment exists, add the desired fragment
            Log.e("testfragment", "exist")
            val newFragment = getintouchFragment()
            newFragment.arguments = bundle

            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.getintouchfragment, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            binding.backstackButton.visibility = View.VISIBLE
            true

        }
        map.overlays.add(marker)

    }


    /* private fun startListeningForWebSocketMessages() {
         CoroutineScope(Dispatchers.IO).launch {
             while (isActive) {
                 // Simulate a message received from the WebSocket
                 val message =
                 Log.d("WebSocketMessage", "Received message: $message")
            //     handleWebSocketMessage(message)
             }
         }
     }*/


    private fun setSecondaryPositionMarker(user : User) { //Singelton

        val marker = Marker(map)
        marker.title = user.firstname+"  " +user.lastname
        marker.id= user._id
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_place_24);
        marker.position = GeoPoint(user.latitude?:10.5,user.longitude?:10.5)
        marker.setOnMarkerClickListener { marker, map ->
            // Show the info fragment
            val bundle = Bundle()
            val existingFragment = childFragmentManager.findFragmentByTag("getintouchFragment")

            Log.e("marker name", marker.title)

            bundle.putString("markerTitle", marker.title)
            bundle.putString("coachid", marker.id)
            // If no existing fragment exists, add the desired fragment
            Log.e("testfragment", "exist")
            val newFragment = getintouchFragment()
            newFragment.arguments = bundle

            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.getintouchfragment, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
            binding.backstackButton.visibility = View.VISIBLE
            true
        }
        map.overlays.add(marker)
    }

    private fun getTuteurs() {
        val request = Request.Builder()
            .url("https://mindfullearn-6od2.onrender.com/api/tuteur")
            .get()// Replace with your API endpoint
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.e("TEESWST","teest")
                    val responseData = response.body?.string() // Get response data as a string

                    responseData?.let {
                        val userListType = object : TypeToken<List<User>>() {}.type
                        val users: List<User> = Gson().fromJson(it, userListType)
                    Log.d("user" , users.toString())
                        // Now you have a list of User objects (users), use it for your map
                        activity?.runOnUiThread {
                            // Use the 'users' list for your map operations
                            // For example, iterate through the 'users' list and add markers to the map
                            for (user in users) {
                                setSecondaryPositionMarker(user)
                                // Add markers to the map using userLatLng
                                // ...
                            }
                        }
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        })
    }

}