package com.example.wecom.ui

import ExerciseAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wecom.R
import com.example.wecom.firestore.MyFirestore
import com.example.wecom.viewmodel.FireStoreViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_group.*

@AndroidEntryPoint
class GroupFragment :Fragment(R.layout.fragment_group){
lateinit var myFirestore: MyFirestore
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myFirestore = MyFirestore()
        myFirestore.retrieveExerciseDistance()
        val adapter = ExerciseAdapter()
         competition_rv.adapter = adapter
         competition_rv.layoutManager = LinearLayoutManager(context)
        val  viewModel =  ViewModelProviders.of(this).get(FireStoreViewModel::class.java)

        viewModel.winnerListbyDis.observe(viewLifecycleOwner, Observer {
            val newList = it
            adapter.list = newList
            adapter.notifyDataSetChanged()
        })
        viewModel.userBestTenList.observe(viewLifecycleOwner, Observer {
            val newList = it
            adapter.list = newList
            adapter.notifyDataSetChanged()
        })

        tabWinner.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.text){
                    "TODAY'S LEADERS" ->{
                       myFirestore.retrieveExerciseDistance()
                    }
                    "MY BEST 10"->{
                        myFirestore.retrieveUserExercise()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("tabb","eee")
            }

        })
    }


}