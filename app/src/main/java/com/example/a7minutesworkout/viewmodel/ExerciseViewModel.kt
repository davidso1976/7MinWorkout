package com.example.a7minutesworkout.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.a7minutesworkout.model.ExerciseModel
import com.example.a7minutesworkout.repository.ExerciseRepository
import com.example.a7minutesworkout.ui.event.ExerciseUIEvent
import com.example.a7minutesworkout.utils.isInRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lib.events.EventBus
import javax.inject.Inject

class ExerciseViewModel : ViewModel(), DefaultLifecycleObserver  {

    @Inject
    lateinit var uiEventBus: EventBus<ExerciseUIEvent>

    @Inject
    lateinit var exerciseRepository: ExerciseRepository

    val timeInterval = 100L
    val restingTime = 5
    val exerciseTimerDuration = 10
    private val currentExerciseIdx = MutableLiveData(-1)
    private val nextExerciseIdx = MutableLiveData(0)


    val restingProgress = MutableLiveData(0)
    val restingInProgress = MutableLiveData(true)
    val restingTimeUntilFinished = MutableLiveData(restingTime.toString())
    val exerciseProgress = MutableLiveData(0)
    val exerciseTimeUntilFinished = MutableLiveData(restingTime.toString())
    private val defaultList = MutableLiveData<List<ExerciseModel>>()

    val exerciseList = MediatorLiveData<List<ExerciseModel>?>().apply {
        var oldList :  List<ExerciseModel> = emptyList()
        fun update(newList: List<ExerciseModel>) {
            oldList = newList
            value = newList
        }

        addSource(currentExerciseIdx) {
            if(it.isInRange(oldList.size)) {
                oldList[it].isSelected = true
                update(oldList)
            }
        }

        addSource(nextExerciseIdx) {
            val currentExerciseIdx = currentExerciseIdx.value ?: -1
            if(currentExerciseIdx.isInRange(oldList.size) ) {
                oldList[currentExerciseIdx].isCompleted = true
                update(oldList)
            }
        }
        addSource(defaultList) {
            update(it)
        }
    }

    private val restingTimer = object: CountDownTimer(restingTime * 1000L, timeInterval){
        override fun onTick(millisUntilFinished: Long) {
            val secondsUntilFinished = (millisUntilFinished/1000).toInt()
            restingProgress.value = restingTime - secondsUntilFinished
            restingTimeUntilFinished.value = secondsUntilFinished.toString()
        }

        override fun onFinish() {
            stopResting()
            startExercise()
        }

    }

    private val exerciseTimer = object: CountDownTimer(exerciseTimerDuration * 1000L, timeInterval){
        override fun onTick(millisUntilFinished: Long) {
            val secondsUntilFinished = (millisUntilFinished/1000).toInt()
            exerciseProgress.value = exerciseTimerDuration - secondsUntilFinished
            exerciseTimeUntilFinished.value = secondsUntilFinished.toString()
        }

        override fun onFinish() {
            stopExercise()
            advanceNextExercise()
            startResting()
        }

    }

    val currentExercise = MediatorLiveData<ExerciseModel?>().apply {
        fun update(exerciseModel: ExerciseModel?) {
            value = exerciseModel
        }
        addSource(currentExerciseIdx) {
            val exerciseModel = exerciseList.value?.getOrNull(it)
            update(exerciseModel)
        }
        addSource(exerciseList) {
            currentExerciseIdx.value?.let { idx ->
                val exerciseModel = it?.getOrNull(idx)
                update(exerciseModel)
            }
        }
    }

    val nextExercise = MediatorLiveData<ExerciseModel?>().apply {
        fun update(exerciseModel: ExerciseModel?) {
            value = exerciseModel
        }
        addSource(nextExerciseIdx) {
            val exerciseModel = exerciseList.value?.getOrNull(it)
            update(exerciseModel)
        }
        addSource(exerciseList) {
            nextExerciseIdx.value?.let { idx ->
                val exerciseModel = it?.getOrNull(idx)
                update(exerciseModel)
            }
        }
    }



    fun advanceNextExercise() {
        val listSize = exerciseList.value?.size ?: -1
        val nextIdx = nextExerciseIdx.value ?: -1
        if(nextIdx + 1 >= listSize) {
            triggerUIEvent(ExerciseUIEvent.FinishExercise)
        } else {
            nextExerciseIdx.value = currentExerciseIdx.value?.plus(1) ?: 0
        }
        currentExerciseIdx.value = -1
    }

    fun startExercise() {
        currentExerciseIdx.value = nextExerciseIdx.value
        exerciseTimer.start()
    }
    fun stopExercise() {
        exerciseTimer.cancel()
    }
    fun startResting() {
        restingInProgress.value = true
        restingTimer.start()
    }
    fun stopResting() {
        restingInProgress.value = false
        restingTimer.cancel()
    }

    private val exerciseUIEventBusBlock : suspend CoroutineScope.() -> Unit = {
        uiEventBus.events.collect { event ->
            when(event) {
                ExerciseUIEvent.FinishExercise -> println("It should all finish now")
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        with(owner.lifecycleScope) {
            launch(block = exerciseUIEventBusBlock)
        }
    }

    private fun triggerUIEvent(event: ExerciseUIEvent) = viewModelScope.launch{
        if(::uiEventBus.isInitialized) {
            uiEventBus.publishEvent(event)
        }
    }

    fun fetchExerciseList() = viewModelScope.launch {
        exerciseRepository.fetchExerciseList().fold({
            Log.d("API", "Error fetching exercise List error = $it")
        }, {
            Log.d("API", "Successfully loading exerciseList = $it")
            it.data?.exerciseList?.let { list ->
                defaultList.postValue(list)
                beginWorkOut()
            }
        })
    }

    private fun beginWorkOut() {
        startResting()
    }

}