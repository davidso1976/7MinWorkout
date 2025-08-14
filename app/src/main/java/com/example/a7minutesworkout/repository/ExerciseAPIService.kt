package com.example.a7minutesworkout.repository

import com.example.a7minutesworkout.model.ExerciseListResponse
import retrofit2.http.GET

interface ExerciseAPIService {

    @GET("api/exercises")
    suspend fun getExerciseList() : ExerciseListResponse
}