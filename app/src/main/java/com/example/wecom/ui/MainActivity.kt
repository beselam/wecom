package com.example.wecom.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wecom.R
import com.example.wecom.others.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
// the main activity which hosts the fragments
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        bottomNavigationView.background = null
        navHostFragment.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.homeFragment2, R.id.settingFragment3, R.id.groupFragment, R.id.locationTrackerFragment ->{
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> bottomNavigationView.visibility = View.GONE
            }
        }


    }

    // if the activity is already created this will check the intent
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent!!)
    }

    // chech the intent for a secific action which leads to the location tracking fragment
    fun checkIntent (intent: Intent){
        intent?.let {
           if(it.action == Constants.SHOW_TRACKING_FRAGMENT)
          navHostFragment.findNavController().navigate(R.id.to_location_trackingFragment)
        }
    }

}