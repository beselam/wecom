package com.example.wecom.firestore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.wecom.others.AppUtility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirestore {

    val exerciseCollectionRef= Firebase.firestore.collection("exercise")
    val auth = FirebaseAuth.getInstance()

    companion object{
        val winnerListByDistance = MutableLiveData<MutableList<ExerciseFstore>>()
        val winnerListByCalorie = MutableLiveData<MutableList<ExerciseFstore>>()
        val userDataList = MutableLiveData<MutableList<ExerciseFstore>>()

    }

    fun saveExerciseToFstore(exerciseFbase: ExerciseFstore) = CoroutineScope(Dispatchers.IO).launch {
       Log.d("exxx",exerciseFbase.toString())
        exerciseCollectionRef.add(exerciseFbase).addOnSuccessListener {
            retrieveExerciseDistance()
           retrieveUserExercise()

    }.addOnFailureListener {
        Log.d("xerror firestore upload","eerrrr")
    }
    }

    fun getUser(): FirebaseUser?{
        auth?.let {
            if (it.currentUser != null){
                return it.currentUser
            }
        }
        return null
    }

    fun getUserId():String?{
        auth?.let {
            return it.currentUser!!.uid
        }
        return null
    }




 fun retrieveUserExercise() = CoroutineScope(Dispatchers.IO).launch {
        val id = getUserId()

        val list = mutableListOf<ExerciseFstore>()
        try {
            exerciseCollectionRef
                .whereEqualTo("idUser",id)
                .orderBy("distanceInMeters",Query.Direction.DESCENDING)
                .limit(10)
                .get().addOnSuccessListener {document->
                if (document != null) {
                    for (exercise in document){
                        var exerciseFbase = exercise.toObject<ExerciseFstore>()
                        list.add(exerciseFbase)
                        Log.d("mfireee", exerciseFbase.toString()
                        )
                    }
                    userDataList.postValue(list)
                    Log.d("mfireData", "DocumentSnapshot data: " +
                            "}")
                } else {
                    Log.d("fireData", "No such document")
                }

            }.addOnFailureListener {
                Log.d("fireData", "No such document")
            }
        }catch (e:Exception){

        }
    }


    fun retrieveExerciseDistance() = CoroutineScope(Dispatchers.IO).launch {
        val date = AppUtility.getFormatedDay()

        val list = mutableListOf<ExerciseFstore>()
        try {
            exerciseCollectionRef
                .whereEqualTo("exerciseDate",date)
                .orderBy("distanceInMeters", Query.Direction.DESCENDING)
                .limit(10)
                .get().addOnSuccessListener {document->
                    if (document != null) {
                        for (exercise in document){
                            var exerciseFbase = exercise.toObject<ExerciseFstore>()
                            list.add(exerciseFbase)
                            Log.d("mfireee", exerciseFbase.toString()
                            )
                        }
                        winnerListByDistance.postValue(list)
                        Log.d("mfireData", "DocumentSnapshot data: " +
                                "}")
                    } else {
                        Log.d("fireData", "No such document")
                    }

                }.addOnFailureListener {
                    Log.d("fireData", "No such document")
                }
        }catch (e:Exception){

        }
    }
}