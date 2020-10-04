package com.example.wecom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wecom.db.Exercise
import com.example.wecom.others.Constants.NODE_RUNS
import com.google.firebase.database.FirebaseDatabase

class RunsViewModel {

    private val _result = MutableLiveData<List<Exercise>>()
     val result: LiveData<Exercise>
        get() = result

    fun addRun(frun: Exercise){
        val dbRuns = FirebaseDatabase.getInstance().getReference(NODE_RUNS)
        frun.id = dbRuns.push().key?.toInt()
        dbRuns.child(frun.id.toString()).setValue(frun)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _result.value = null
                }else{
                    //_result.value = it.exception
                    return@addOnCompleteListener
                }
            }
    }
}