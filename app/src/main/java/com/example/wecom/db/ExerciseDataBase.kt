package com.example.wecom.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Exercise::class],version = 1)
abstract class ExerciseDataBase:RoomDatabase() {
    abstract fun getExerciseDao(): ExerciseDao
}