package com.example.wecom.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wecom.R
import com.example.wecom.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.bottom_sheet.*

class ReviewFragment :Fragment(R.layout.fragment_review){
    private val viewModel: MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}