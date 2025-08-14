package com.example.a7minutesworkout.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.a7minutesworkout.model.HistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>
}
