package com.example.wecom.db

import androidx.lifecycle.LiveData
import androidx.room.*

// a Dao class for manipulating the room database
@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertExercise(exercise:Exercise)

    @Delete
    suspend fun deleteExercise(exercise:Exercise)

    @Query("SELECT * FROM exercise_db_tb ORDER BY exerciseDate DESC")
    fun getAllEXercisesSortedByDate(): LiveData<List<Exercise>>

    @Query("SELECT SUM(exerciseTimeInMillis) FROM exercise_db_tb")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM exercise_db_tb")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM exercise_db_tb")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(averageSpeed) FROM exercise_db_tb")
    fun getTotalAvgSpeed(): LiveData<Float>
}