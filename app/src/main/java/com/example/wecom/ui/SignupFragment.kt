package com.example.wecom.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignupFragment:Fragment(R.layout.fragment_sign_up) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToSignIn_bt.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        createAccount_bt.setOnClickListener {
            navHostFragment.findNavController().navigate(R.id.action_signUpFragment_to_homeFragment2)
        }
    }
}