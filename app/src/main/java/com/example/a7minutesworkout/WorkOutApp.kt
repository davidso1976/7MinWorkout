package com.example.a7minutesworkout

import android.app.Application
import dagger.Component

@Component
interface ApplicationComponent {

    fun inject(activity: ExerciseActivity)

}

class WorkOutApp : Application() {

    val db by lazy{
        HistoryDatabase.getInstance(this)
    }
    val appComponent = DaggerApplicationComponent.create()
}