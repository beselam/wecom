package com.example.wecom.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.wecom.R
import com.example.wecom.db.Exercise
import com.example.wecom.firestore.ExerciseFstore
import com.example.wecom.firestore.MyFirestore
import com.example.wecom.others.AppUtility
import com.example.wecom.others.Constants.PAUSE_SERVICE
import com.example.wecom.others.Constants.START_SERVICE
import com.example.wecom.others.Constants.STOP_SERVICE
import com.example.wecom.service.AppMapService
import com.example.wecom.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_group.*
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackerFragment : Fragment(R.layout.fragment_location_tracker) {



    private  val  myFirestore= MyFirestore()
    private var map: GoogleMap? = null
    private var isServiceGoing = false
    private var isServicePaused = false
    private var exerciseTimeInMillis = 0L
    private var weight = 80f
    private val viewModel: MainViewModel by viewModels()
    private var locationData = mutableListOf(mutableListOf<LatLng>())



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleButton(isServiceGoing, isServicePaused)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            createAllPolylines()
        }

        start_bt.setOnClickListener {

            comandToAppMapService(START_SERVICE)
        }
        resume_bt.setOnClickListener {
            comandToAppMapService(START_SERVICE)
        }
        pause_bt.setOnClickListener {
            comandToAppMapService(PAUSE_SERVICE)

        }
        cancel_bt.setOnClickListener {
          //  comandToAppMapService(PAUSE_SERVICE)
            cancelExersice(false)
        }
        finish_exercise_bt.setOnClickListener {
           // comandToAppMapService(STOP_SERVICE)
            cancelExersice(true)
        }

        subscribeToObservers()
    }



    private fun cancelExersice(saveToDb:Boolean) {
        var title = "cancel exercise"
        var message = "are sure you need to cancel the exercise with out saving "
        if (saveToDb){
            title = "finish exercise"
            message = "are you sure you want to finish the exercise"
        }
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(message)
        builder.setIcon(R.drawable.ic_direction)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            if (saveToDb) {
                endRunAndSaveToDb()}
            comandToAppMapService(STOP_SERVICE)
        }
        //performing cancel action
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            comandToAppMapService(START_SERVICE)
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            comandToAppMapService(START_SERVICE)
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun toggleButton(serviceStarted: Boolean, servicePaused: Boolean) {
        Log.d("service", serviceStarted.toString())
        if (serviceStarted && !servicePaused) {
            start_bt.visibility = GONE
            finish_exercise_bt.visibility = VISIBLE
            pause_bt.visibility = VISIBLE
            cancel_bt.visibility = VISIBLE
            resume_bt.visibility = GONE
        } else if (!serviceStarted && servicePaused) {
            start_bt.visibility = GONE
            finish_exercise_bt.visibility = VISIBLE
            pause_bt.visibility = GONE
            resume_bt.visibility = VISIBLE
            cancel_bt.visibility = VISIBLE
        } else {
            start_bt.visibility = VISIBLE
            finish_exercise_bt.visibility = GONE
            pause_bt.visibility = GONE
            cancel_bt.visibility = GONE
            resume_bt.visibility = GONE

        }
    }

    private fun comandToAppMapService(comand: String) {
        Intent(requireContext(), AppMapService::class.java).also {
            it.action = comand
            requireContext().startService(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    /* override fun onMapReady(map: GoogleMap){
       map.addMarker(MarkerOptions().position(locationData[0][0]))
   }*/
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    private fun createAllPolylines() {
        for (mutableList in locationData) {
            val polylineOptions = PolylineOptions()
                .color(R.color.colorPrimary)
                .width(9f)
                .addAll(mutableList)
            map?.addPolyline(polylineOptions)
        }
    }


    private fun createNewPolyline() {
        if (locationData.isNotEmpty() && locationData.last().size > 1) {
            val secondLastLatLng = locationData.last()[locationData.last().size - 2]
            val lastLatLng = locationData.last().last()
            val polylineOptions = PolylineOptions()
                .color(R.color.colorPrimary)
                .width(9f)
                .add(secondLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun moveCameraToLatesLocation() {
        if (locationData.isNotEmpty() && locationData.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    locationData.last().last(),
                    15f
                )
            )
        }
    }


    private fun subscribeToObservers() {
        AppMapService.isServiceGoing.observe(viewLifecycleOwner, Observer {
            isServiceGoing = it
            toggleButton(isServiceGoing, isServicePaused)
        })

        AppMapService.locationData.observe(viewLifecycleOwner, Observer {
            locationData = it
            createNewPolyline()
            moveCameraToLatesLocation()
        })
        AppMapService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            exerciseTimeInMillis = it
            val exerciseTimer = AppUtility.createFormattedTime(exerciseTimeInMillis, true)
            timer_tv.text = exerciseTimer

        })
        AppMapService.isServicePaused.observe(viewLifecycleOwner, Observer { service ->
            isServicePaused = service
            toggleButton(isServiceGoing, isServicePaused)
        })

    }
// move the google map camera to user total polyline

    private fun endRunAndSaveToDb() {
        Log.d("dataaa distance","$locationData")
            var distanceInMeters = 0
            for(polyline in locationData) {
                distanceInMeters += AppUtility.calculatePolylineLength(polyline).toInt()
                Log.d("dataaa distance","$distanceInMeters")
            val avgSpeed = Math.round((distanceInMeters / 1000f) / (exerciseTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val exercise = Exercise(dateTimestamp, distanceInMeters,caloriesBurned,avgSpeed,exerciseTimeInMillis)
                val date = AppUtility.getFormatedDay()
                val userId = myFirestore.getUserId()
                var exerciseFs = ExerciseFstore(userId!!,"run",date,distanceInMeters,caloriesBurned,avgSpeed,exerciseTimeInMillis)
                viewModel.insertExercise(exercise)
                myFirestore.saveExerciseToFstore(exerciseFs)

            Snackbar.make(
                requireActivity().findViewById(R.id.mainActivity),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }



}






