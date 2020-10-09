package com.example.wecom.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecom.db.Exercise
import com.example.wecom.dependency_injection.BaseApplication
import com.example.wecom.repository.BaseRepository
import kotlinx.coroutines.launch
 // viewmodel for the room database
class MainViewModel @ViewModelInject constructor(val repository: BaseRepository
): ViewModel()  {
   fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        repository.insertExercise(exercise)
   }

    fun getAllExersiseSortedByDate() = repository.getAllExercisesSortedByDate()
    suspend fun deleteExercise(exercise: Exercise) = repository.deleteExercise(exercise)
}
