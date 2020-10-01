package com.example.wecom.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.wecom.others.AppUtility
import com.example.wecom.others.Constants.NOTIFICATION_CHANNEL_ID
import com.example.wecom.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.wecom.others.Constants.NOTIFICATION_ID
import com.example.wecom.others.Constants.PAUSE_SERVICE
import com.example.wecom.others.Constants.START_SERVICE
import com.example.wecom.others.Constants.STOP_SERVICE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




@AndroidEntryPoint
class AppMapService : LifecycleService() {


    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
   @Inject
   lateinit var fusedLocationProviderClient:FusedLocationProviderClient

     private  var  serviceFistStart = true
companion object{
    val isServiceGoing = MutableLiveData<Boolean>()
    val locationData = MutableLiveData<MutableList<MutableList<LatLng>>>()


}

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

         isServiceGoing.observe(this, Observer {
         updateLocationTracking(it)

     })
    }
    private fun postInitialValues() {
        isServiceGoing.postValue(false)
        locationData.postValue(mutableListOf())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                START_SERVICE -> {
                    if (serviceFistStart){
                        Log.d("servicc","service started")
                        startTrackingForegroundService()
                    }else{
                        Log.d("servicc","service paused")
                    }

                }
                STOP_SERVICE -> {
                    Log.d("servicc","service stop")
                }

                PAUSE_SERVICE -> {
                    Log.d("servicc","service paused")
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }
// starting the foreground service
    fun startTrackingForegroundService(){
      addEmptyPolyline()
    isServiceGoing.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
    startForeground(NOTIFICATION_ID,baseNotificationBuilder.build())
    }

// create a notification channel
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


    private fun addEmptyPolyline() = locationData.value?.apply {
        add(mutableListOf())
        locationData.postValue(this)
    } ?: locationData.postValue(mutableListOf(mutableListOf()))


    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            locationData.value?.apply {
                last().add(pos)
                locationData.postValue(this)
            }
        }
    }

    // get a new location when a user moves and send that new location addPathponit function
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            result?.locations?.let { locations ->
                Log.d("newlocationssss","$locations")
                Log.d("newlocationssss","${locations.size}")
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d("newlocation","$location")
                    }
                }

        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            Log.d("is tracking","is tracking classed")
            Log.d("is tracking","${AppUtility.locationPermissions(this)}")
            if (AppUtility.locationPermissions(this)) {
                Log.d("is tracking","has permission")
                val request = LocationRequest().apply {
                    interval = 5000L
                    fastestInterval = 3000L
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                // request a new location
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            Log.d("is tracking","no permission")
        }
    }

}