package lib.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow


interface EventBus<T> {
    val events: SharedFlow<T>
    val subscriptionCount: StateFlow<Int>
    suspend fun publishEvent(event: T)

    companion object {
        inline fun <reified T> default(): EventBus<T> = object : EventBus<T> {
            private val mutableEvents = MutableSharedFlow<T>()
            override val events: SharedFlow<T> = mutableEvents.asSharedFlow()
            override val subscriptionCount: StateFlow<Int> = mutableEvents.subscriptionCount

            override suspend fun publishEvent(event: T) {
                mutableEvents.emit(event)
            }
        }
    }
}

interface StatefulEventBus<T> : EventBus<T> {
    fun getLastEvent(): T?

    companion object {
        inline fun <reified T> default(): StatefulEventBus<T> = object : StatefulEventBus<T> {
            private val mutableEvents = MutableSharedFlow<T>()
            override val events: SharedFlow<T> = mutableEvents.asSharedFlow()
            override val subscriptionCount: StateFlow<Int> = mutableEvents.subscriptionCount
            private var lastEvent: T? = null
            override suspend fun publishEvent(event: T) {
                lastEvent = event
                mutableEvents.emit(event)
            }

            override fun getLastEvent(): T? {
                return lastEvent
            }
        }
    }
}