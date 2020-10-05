package com.example.wecom.ui


import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment:Fragment(R.layout.fragment_splash) {
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            auth?.let {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()
                findNavController().navigate(
                    R.id.to_home_screen,
                    savedInstanceState,
                    navOptions
                )
            }
        }

        val animtop =  AnimationUtils.loadAnimation(activity, R.anim.top_animation)
        val animbt =  AnimationUtils.loadAnimation(activity, R.anim.bottom_animation)
        val animFromLeft =  AnimationUtils.loadAnimation(activity, R.anim.slid_from_left_animation)
        val animFromRight =  AnimationUtils.loadAnimation(
            activity,
            R.anim.slid_from_right_animation
        )
        animm.animation = animtop
        spalshLogoTv.animation = animFromLeft
        appMotoTv.animation = animFromRight

        signinBt.setOnClickListener {
           // val imageViewPair = Pair.create<View, String>(signinBt, getString("loginTransition"))
           // val extras = FragmentNavigatorExtras( signinBt to "loginTransition")



            val extras = FragmentNavigatorExtras(
                signinBt to "loginTransition"
            )


            navHostFragment.findNavController().navigate(R.id.action_splashFragment_to_signInFragment,null,null,extras)

        }

    }

    override fun onStart() {
        super.onStart()

    }

}