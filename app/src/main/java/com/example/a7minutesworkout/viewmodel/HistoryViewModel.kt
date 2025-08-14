package com.example.a7minutesworkout.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a7minutesworkout.model.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel() : ViewModel(), DefaultLifecycleObserver {

    val exerciseList = MutableLiveData<List<HistoryEntity>>()

    val showNoData = MutableLiveData<Boolean>(true)

    fun fetchAllDates() = viewModelScope.launch {
//        if(::historyDao.isInitialized){
//            historyDao.fetchAllDates().collect { allCompletedDatesList ->
//                exerciseList.value = allCompletedDatesList
//                if(allCompletedDatesList.isNotEmpty()) {
//                    showNoData.value = false
//                }
//            }
//        }

    }

}