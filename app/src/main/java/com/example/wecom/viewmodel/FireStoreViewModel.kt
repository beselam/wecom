package com.example.wecom.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wecom.firestore.ExerciseFstore
import com.example.wecom.firestore.MyFirestore

class FireStoreViewModel:ViewModel() {
      val firestore = MyFirestore
      val winnerListbyDis = firestore.winnerListByDistance
      var userBestTenList = firestore.userDataList
      // val runList = firestore.winnerListByDistance






}