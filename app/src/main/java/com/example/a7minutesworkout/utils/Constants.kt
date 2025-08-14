package com.example.a7minutesworkout.utils

import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.model.ExerciseModel

object URIConstants {
    const val RESOURCE_URI = "android.resource://com.example.a7minutesworkout/"
}

object DefaultValues {
    fun defaultExerciseList() : List<ExerciseModel>{
        val exerciseList = mutableListOf<ExerciseModel>()
        return exerciseList
    }
}