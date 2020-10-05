package com.example.wecom.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirestore {
    val exerciseCollectionRef= Firebase.firestore.collection("exercise")

    fun saveExerciseToFstore(exerciseFbase: ExerciseFstore) = CoroutineScope(Dispatchers.IO).launch {
    exerciseCollectionRef.add(exerciseFbase).addOnSuccessListener {
        Log.d("xerror firestore upload","ee")
    }.addOnFailureListener {
        Log.d("xerror firestore upload","eerrrr")
    }
    }


}