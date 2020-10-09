package com.example.wecom.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import com.example.wecom.firestore.MyFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.email_profile_et
import kotlinx.android.synthetic.main.fragment_setting.username_profile_et
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// a  class for displaying user profile data
class profileFragment : Fragment(R.layout.fragment_setting) {

    lateinit var myFirestore: MyFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myFirestore = MyFirestore()


        if (myFirestore.getUser() != null) {
            val user = myFirestore.getUser()
            setupUi(user!!)
        }
        profile_update_email.setOnClickListener(setClickListner())
        profile_update_weight.setOnClickListener(setClickListner())
        profile_update_name.setOnClickListener(setClickListner())
        profile_logout_bt.setOnClickListener(setClickListner())
    }

    // set up user data to the view
    fun setupUi(user: FirebaseUser) {
        Log.d("user", "$user.displayName $user.displayName")
        profile_full_name_field_tv.text = user.displayName
        username_profile_et.setText(user.displayName)
        email_profile_et.setText(user.email)
        weight_profile_et.setText(user.photoUrl.toString())

    }

    private fun validateName(
        username: String
    ): Boolean {

        if (username.isEmpty()) {

            username_profile_et.error = "username field is empty"
            return false
        } else if (username.length > 15) {
            username_profile_et.error = "username to long"
            return false
        } else return true

    }

    private fun validateEmail(
        email: String
    ): Boolean {

        if (email.isEmpty()) {
            email_profile_et.error = "email field is empty"
            return false

        } else return true

    }

    private fun validateWeight(
        weight: String
    ): Boolean {

        if (weight.isEmpty()) {
            weight_profile_et.error = "field is empty"
            return false

        }
        if (weight.length > 3) {
            weight_profile_et.error = "invalid number"
            return false
        } else return true

    }

    fun logoutUser() {
        myFirestore.auth.signOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment, true)
            .build()
        findNavController().navigate(
            R.id.to_home_splash_screen,
            null,
            navOptions
        )

    }

    // click listner
    fun setClickListner() = View.OnClickListener {
        val user = myFirestore.getUser()
        when (it) {
            profile_update_name -> {
                val username = username_profile_et.text.toString()
                val valid = validateName(username)
                if (valid) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            user!!.updateProfile(profileUpdates).addOnSuccessListener {

                                username_profile_et.setText(user.displayName)
                                profile_full_name_field_tv.text = user.displayName
                                Toast.makeText(
                                    requireContext(), "Successfully updated profile",
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }
                }
            }

            profile_update_email -> {
                Log.d("updateemail", "dddd")
                val email = email_profile_et.text.toString()
                val valid = validateEmail(email)
                if (valid) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            user!!.updateEmail(email).addOnSuccessListener {
                                email_profile_et.setText(user.email)
                                Toast.makeText(
                                    requireContext(), "Successfully updated Email",
                                    Toast.LENGTH_LONG
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(), "$it",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }
                }

            }
            profile_update_weight -> {
                val weight = weight_profile_et.text.toString()
                val valid = validateWeight(weight)
                if (valid) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(weight.toUri())
                        .build()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            user!!.updateProfile(profileUpdates).addOnSuccessListener {
                                weight_profile_et.setText(user.photoUrl.toString())
                                Toast.makeText(
                                    requireContext(), "Successfully updated Weight",
                                    Toast.LENGTH_LONG
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(), "$it",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }
                }
            }
            profile_logout_bt -> {
                logoutUser()
            }
        }
    }

}
