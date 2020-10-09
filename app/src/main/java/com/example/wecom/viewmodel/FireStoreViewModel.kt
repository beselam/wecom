package com.example.wecom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecom.firestore.MyFirestore
import kotlinx.coroutines.launch
// viewmodel for the fireStore class
class FireStoreViewModel:ViewModel() {
      val firestore = MyFirestore()
      val winnerListbyDis = firestore.winnerListByDistance
      var userBestTenList = firestore.userExerciseList
      var loading = firestore.loading
      var rank = firestore.userRank
      fun retrieveExerciseDistance() = viewModelScope.launch {
            firestore.retrieveExerciseByDistance()
      }
      fun retrieveUserExercise() = viewModelScope.launch {
            firestore.retrieveUserExercise()
      }
      fun getUserRank() = viewModelScope.launch {
           firestore.getUserRank()
            rank = firestore.userRank
            Log.d("rank","cc${rank.value}")

      }







}