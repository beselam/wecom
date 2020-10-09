package com.example.wecom.ui

import ExerciseAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wecom.R
import com.example.wecom.firestore.ExerciseFstore
import com.example.wecom.firestore.MyFirestore
import com.example.wecom.viewmodel.FireStoreViewModel
import com.google.android.material.tabs.TabLayout
import com.squareup.okhttp.Dispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// a class for user competition with other users
@AndroidEntryPoint
class GroupFragment : Fragment(R.layout.fragment_group) {
    lateinit var winerList: MutableList<ExerciseFstore>
    lateinit var userExerciselist: MutableList<ExerciseFstore>
    lateinit var viewModel: FireStoreViewModel
    var loading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        winerList = mutableListOf()
        userExerciselist = mutableListOf()
        val adapter = ExerciseAdapter()
        competition_rv.adapter = adapter
        competition_rv.layoutManager = LinearLayoutManager(context)


        viewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)
        viewModel.retrieveExerciseDistance()
        viewModel.retrieveUserExercise()

        viewModel.winnerListbyDis.value?.let {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }
        getUserRank()
        viewModel.winnerListbyDis.observe(viewLifecycleOwner, Observer {
            winerList = it
            adapter.list = winerList
            adapter.notifyDataSetChanged()

        })
        viewModel.userBestTenList.observe(viewLifecycleOwner, Observer {
            userExerciselist = it
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            loading = it
        })




        tabWinner.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.text) {
                    "TODAY'S LEADERS" -> {

                        viewModel.retrieveExerciseDistance()
                        adapter.list = winerList
                        adapter.notifyDataSetChanged()
                    }
                    "MY BEST 10" -> {
                        viewModel.retrieveUserExercise()
                        adapter.list = userExerciselist
                        Log.d("mybest", "$userExerciselist")
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("tabb", "eee")
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun getUserRank() {
        val mm = viewModel.getUserRank()
        viewModel.rank.observe(viewLifecycleOwner, Observer {
            Log.d("rank", "$it")
            val rank = it + 1
            user_rank_tv?.text = "Your currunt rank is $rank"
        })

    }

}