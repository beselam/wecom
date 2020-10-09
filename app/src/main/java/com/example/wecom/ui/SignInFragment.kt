package com.example.wecom.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.profile_page_passw_et
import kotlinx.coroutines.*
import java.lang.Exception

// sign in the user
class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        forgetPassword_bt.setOnClickListener {
            val editText = EditText(it.context)
            resetEmail(editText)
        }
        login_Bt.setOnClickListener {
            val email = login_page_email_et.text.toString()
            val password = profile_page_passw_et.text.toString()
            val valid = validateForm(email, password)
            if (valid) {
                signin(email, password)
            }
        }

        login_new_user_bt.setOnClickListener {
            navHostFragment.findNavController()
                .navigate(R.id.action_signInFragment_to_signUpFragment)

        }

    }

    fun resetEmail(editText: EditText) {

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Reset Password")
        //set message for alert dialog
        builder.setMessage("Enter your email to get a reset link")
        builder.setIcon(R.drawable.ic_direction)
        builder.setView(editText)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val email = editText.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener(OnSuccessListener {
                    Toast.makeText(context, "reset link send to your email", Toast.LENGTH_LONG)
                        .show()

                }).addOnFailureListener(OnFailureListener {
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()

                })
            }
        }
        //performing cancel action
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    fun signin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(OnSuccessListener {

                        Toast.makeText(context, "signed in", Toast.LENGTH_LONG).show()
                        navHostFragment.findNavController()
                            .navigate(R.id.action_signInFragment_to_homeFragment2)

                    }).addOnFailureListener(OnFailureListener {
                        Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()


                    })

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun validateForm(
        email: String,
        password: String,
    ): Boolean {

        if (email.isEmpty()) {
            login_page_email_et.error = "email field is empty"
            return false
        } else if (password.isEmpty()) {
            profile_page_passw_et.error = "password field is empty"
            return false
        }

        return true
    }
}