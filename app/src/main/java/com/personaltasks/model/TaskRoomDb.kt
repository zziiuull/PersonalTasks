package com.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskRoomDb: RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
}