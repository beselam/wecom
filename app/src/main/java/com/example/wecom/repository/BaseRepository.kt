package com.example.wecom.repository

import com.example.wecom.db.ExerciseDao
import com.example.wecom.db.Exercise
import javax.inject.Inject
// room repositort
class BaseRepository @Inject constructor(private val exerciseDao: ExerciseDao) {

    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insertExercise(exercise)

    suspend fun deleteExercise(exercise: Exercise) = exerciseDao.deleteExercise(exercise)

    fun getAllExercisesSortedByDate() = exerciseDao.getAllEXercisesSortedByDate()

    fun getTotalAvgSpeed() = exerciseDao.getTotalAvgSpeed()

    fun getTotalDistance() = exerciseDao.getTotalDistance()

    fun getTotalCaloriesBurned() = exerciseDao.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = exerciseDao.getTotalTimeInMillis()

}