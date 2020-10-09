package com.example.wecom.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.example.wecom.R
import com.example.wecom.others.AppUtility
import com.example.wecom.others.Constants.NOTIFICATION_CHANNEL_ID
import com.example.wecom.others.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.wecom.others.Constants.NOTIFICATION_ID
import com.example.wecom.others.Constants.PAUSE_SERVICE
import com.example.wecom.others.Constants.START_SERVICE
import com.example.wecom.others.Constants.STOP_SERVICE
import com.example.wecom.others.Constants.TIME_DELAY
import com.example.wecom.others.Constants.lOCATION_REQUEST_INTERVAL
import com.example.wecom.others.Constants.lOCATION_REQUEST_INTERVAL_FASTEST
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

// foreground service class for tracking location
@AndroidEntryPoint
class AppMapService : LifecycleService() {
     private var  firstStart = true
     private var  serviceStoped = true
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var updatableNotificationBuilder: NotificationCompat.Builder
   @Inject
   lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private val timeRunInSeconds = MutableLiveData<Long>()
    private  var  serviceFistStart = true
    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var secondincrimenter = 0L


companion object {
    val timeRunInMillis = MutableLiveData<Long>()
    val isServiceGoing = MutableLiveData<Boolean>()
    val isServicePaused = MutableLiveData<Boolean>()
    val locationData = MutableLiveData<MutableList<MutableList<LatLng>>>()
}

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        updatableNotificationBuilder = baseNotificationBuilder

         isServiceGoing.observe(this, Observer {
                updateLocationTracking(it)
                updateNotification(it)

     })
    }
    private fun postInitialValues() {
        isServiceGoing.postValue(false)
        locationData.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        isServicePaused.postValue(false)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                START_SERVICE -> {
                        startTrackingForegroundService()
                        Log.d("service","service started")
                }
                STOP_SERVICE -> {
                    stopThisService()
                    Log.d("service","service stoped")
                }

                PAUSE_SERVICE -> {
                    pauseLocationTrackingService()
                    Log.d("servicc","service paused")
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }
// starting the foreground service
    fun startTrackingForegroundService(){
    if (firstStart) {
        firstStart = false
        startTimer()
        isServiceGoing.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        //update notification timestamp
        timeRunInSeconds.observe(this, Observer {
                val notification = updatableNotificationBuilder
                    .setContentText(AppUtility.createFormattedTime(it * 1000L))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
        })

    }
    else{
      startTimer()
    }

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
                Log.d("location data ","${locationData.value}")
            }
        }
    }

    // get a new location when a user moves and send that new location addPathponit function
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            Log.d("request",result.toString())
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
    private fun updateLocationTracking(isServiceGoing: Boolean) {
        Log.d("ddd","herrrrrrrrrrrrr")
        if (isServiceGoing) {
            Log.d("is tracking","is tracking classed")
            Log.d("is tracking","${AppUtility.locationPermissions(this)}")
            if (AppUtility.locationPermissions(this)) {
                Log.d("is tracking","has permission")
                val request = LocationRequest().apply {
                    interval = lOCATION_REQUEST_INTERVAL
                    fastestInterval = lOCATION_REQUEST_INTERVAL_FASTEST
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                Log.d("is tracking","")
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

    fun pauseLocationTrackingService(){
        isServiceGoing.value = false
        isServicePaused.postValue(true)
        isTimerEnabled = false
    }


    private fun startTimer() {
        addEmptyPolyline()
        isServicePaused.postValue(false)
        isServiceGoing.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isServiceGoing.value!!) {
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // post the new lapTime
                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= secondincrimenter + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    secondincrimenter += 1000L
                }
                delay(TIME_DELAY)
            }
            timeRun += lapTime
        }
    }

// updating the notification
    private fun updateNotification(isTracking: Boolean) {
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, AppMapService::class.java).apply {
                action = PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, AppMapService::class.java).apply {
                action = START_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // remove old actions
        updatableNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(updatableNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // create an updated noticication with a new action

        updatableNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic_direction, notificationActionText, pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, updatableNotificationBuilder.build())

    }

    private fun stopThisService() {
        serviceStoped = true
        firstStart = true
        isServiceGoing.postValue(false)
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

}