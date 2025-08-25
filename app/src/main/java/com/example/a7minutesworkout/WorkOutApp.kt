package com.example.a7minutesworkout

import android.app.Application
import com.example.a7minutesworkout.activity.ExerciseActivity
import com.example.a7minutesworkout.activity.History
import com.example.a7minutesworkout.modules.ExerciseModule
import com.example.a7minutesworkout.modules.OkHttpModule
import com.example.a7minutesworkout.ui.event.ExerciseUIEvent
import com.example.a7minutesworkout.viewmodel.ExerciseViewModel
import com.example.a7minutesworkout.viewmodel.HistoryViewModel
import dagger.Component
import lib.events.EventBus
import javax.inject.Singleton

@Singleton
@Component(modules = [OkHttpModule::class, ExerciseModule::class] )
interface ApplicationComponent {

    fun inject(exerciseActivity: ExerciseActivity)
    fun inject(exerciseViewModel: ExerciseViewModel)
    fun inject(historyActivity: History)
    fun inject(historyViewModel: HistoryViewModel)
    fun inject(eventBus: EventBus<ExerciseUIEvent>)
}

class WorkOutApp : Application() {
    val appComponent = DaggerApplicationComponent.create()
}