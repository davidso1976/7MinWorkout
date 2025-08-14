package com.example.a7minutesworkout.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a7minutesworkout.model.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}