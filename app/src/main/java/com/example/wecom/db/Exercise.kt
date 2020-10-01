package com.example.wecom.db

import androidx.room.Entity
import androidx.room.PrimaryKey


// a database table
@Entity(tableName = "exercise_db_tb")
data class Exercise(
     var exerciseDate: Long = 0L,
     var distanceInMeters: Int = 0,
     var caloriesBurned: Int = 0,
     var averageSpeed: Float = 0f,
     var exerciseTimeInMillis: Long = 0L

) {
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
}