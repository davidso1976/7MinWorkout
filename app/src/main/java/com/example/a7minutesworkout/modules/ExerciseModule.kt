package com.example.a7minutesworkout.modules

import com.example.a7minutesworkout.database.HistoryDatabase
import com.example.a7minutesworkout.repository.ExerciseAPIService
import com.example.a7minutesworkout.repository.ExerciseRepository
import com.example.a7minutesworkout.ui.event.ExerciseUIEvent
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import lib.events.EventBus
import lib.utils.buildRetrofit
import okhttp3.OkHttpClient
import javax.inject.Singleton

@DisableInstallInCheck
@Module
open class ExerciseModule {

    @Provides
    @Singleton
    open fun provideMoshi() : Moshi = Moshi.Builder()
        .build()

    @Provides
    @Singleton
    open fun provideExerciseAPIService(okHttpClient: OkHttpClient, moshi: Moshi) : ExerciseAPIService {
        return buildRetrofit(BASE_URL, okHttpClient)
    }

    @Provides
    @Singleton
    open fun provideUiEventBus() : EventBus<ExerciseUIEvent> = object : EventBus<ExerciseUIEvent> {
        private val mutableEvents = MutableSharedFlow<ExerciseUIEvent>()
        override val events = mutableEvents.asSharedFlow()
        override val subscriptionCount: StateFlow<Int> = mutableEvents.subscriptionCount

        override suspend fun publishEvent(event: ExerciseUIEvent) {
            mutableEvents.emit(event)
        }
    }

    @Provides
    @Singleton
    open fun provideExerciseRepository(exerciseAPIService: ExerciseAPIService) : ExerciseRepository {
        return ExerciseRepository(exerciseAPIService)
    }

    companion object {
        const val BASE_URL = "http://localhost:8080/"
    }

}