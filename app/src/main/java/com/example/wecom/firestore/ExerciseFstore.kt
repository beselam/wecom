package com.example.wecom.firestore
// dataclass for firestore
data class ExerciseFstore (
    var idUser:String = "",
    var extype:String="run",
    var exerciseDate: String = "00",
    var distanceInMeters: Int = 0,
    var caloriesBurned: Int = 0,
    var averageSpeed: Float = 0f,
    var exerciseTimeInMillis: Long = 0L
)