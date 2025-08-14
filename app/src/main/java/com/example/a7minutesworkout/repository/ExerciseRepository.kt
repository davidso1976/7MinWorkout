package com.example.a7minutesworkout.repository

import android.util.Log
import arrow.core.Either
import com.example.a7minutesworkout.model.ExerciseListResponse

open class ExerciseRepository(private val exerciseAPIService: ExerciseAPIService) {
    open suspend fun fetchExerciseList(): Either<Throwable, ExerciseListResponse> {
        return Either.catch {
            Log.d("API", "Calling Fetch Exercise List")
            exerciseAPIService.getExerciseList().also {
                Log.d("API", "Se logroooo")
            }
        }
    }
}