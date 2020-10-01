package com.example.wecom.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment:Fragment(R.layout.fragment_sign_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgetPassword_bt.setOnClickListener {

            navHostFragment.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        login_Bt.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_signInFragment_to_homeFragment2)
        }
    }
}