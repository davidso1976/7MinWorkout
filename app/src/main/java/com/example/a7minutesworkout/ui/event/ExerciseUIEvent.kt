package com.example.a7minutesworkout.ui.event

sealed class ExerciseUIEvent {
    data object FinishExercise : ExerciseUIEvent()
}