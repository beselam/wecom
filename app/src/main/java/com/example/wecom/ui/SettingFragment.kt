package com.example.wecom.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.email_profile_et
import kotlinx.android.synthetic.main.fragment_setting.profile_upfate_bt
import kotlinx.android.synthetic.main.fragment_setting.username_profile_et


class SettingFragment :Fragment(R.layout.fragment_setting){
    lateinit var auth:FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
      if (auth.currentUser != null){
          val user = auth.currentUser
            setupUi(user!!)
      }

        profile_upfate_bt.setOnClickListener {

        }
        profile_logout_bt.setOnClickListener {
        logoutUser()
        }
    }


    fun setupUi(user:FirebaseUser) {
        Log.d("user","$user.displayName $user.displayName")
        profile_full_name_field_tv.text = user.displayName
        username_profile_et.setText(user.displayName)
        email_profile_et.setText(user.email)

    }

    private fun updateProfile(user: FirebaseUser) {

     /*  user?.let { user ->
            val username =  username_profile_et.text.toString()
            val email =  email_profile_et.text.toString()

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdates).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Successfully updated profile",
                            Toast.LENGTH_LONG).show()
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        } */
    }

    private fun validateForm(
        email: String,
        username: String
    ): Boolean {

        if (username.isEmpty()) {
            username_profile_et.error = "username field is empty"
            return false
        } else if (username.length > 15) {
            username_profile_et.error = "username to long"
            return false
        } else if (email.isEmpty()) {
            email_profile_et.error = "email field is empty"
            return false

        }
        else return true

    }
    fun logoutUser(){
        auth.signOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment, true)
            .build()
        findNavController().navigate(
            R.id.to_home_splash_screen,
            null,
            navOptions
        )

    }

}
