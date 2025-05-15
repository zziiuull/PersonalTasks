package com.personaltasks.controller

import androidx.room.Room
import com.personaltasks.model.Task
import com.personaltasks.model.TaskDAO
import com.personaltasks.model.TaskRoomDb
import com.personaltasks.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainController(mainActivity: MainActivity) {
    private val taskDAO: TaskDAO = Room.databaseBuilder(
        mainActivity,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDAO()

    fun insertTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO){
                taskDAO.createTask(task)
            }
        }
    }
    fun getTask(id: Int) = taskDAO.retrieveTask(id)
    fun getTasks() = taskDAO.retrieveTasks()
    fun modifyTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO){
                taskDAO.updateTask(task)
            }
        }
    }
    fun removeTask(task: Task) {
        MainScope().launch {
            withContext(Dispatchers.IO){
                taskDAO.deleteTask(task)
            }
        }
    }
}