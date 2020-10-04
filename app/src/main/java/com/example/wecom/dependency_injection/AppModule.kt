package com.example.wecom.dependency_injection

import android.content.Context
import androidx.room.Room
import com.example.wecom.db.ExerciseDataBase
import com.example.wecom.others.Constants.DATABASE_NAME
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) // used all over the app
 object AppModule {

    @Singleton
    @Provides
  fun provideExerciseDtabase(@ApplicationContext app:Context)= Room.databaseBuilder(app,
      ExerciseDataBase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideExerciseDao(db:ExerciseDataBase) = db.getExerciseDao()

    @Singleton
    @Provides
    fun provideFirebaseDatabase():FirebaseDatabase{
        return FirebaseDatabase.getInstance()
    }
}