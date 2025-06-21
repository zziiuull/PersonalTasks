package com.personaltasks.controller

import android.os.Message
import com.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.personaltasks.model.Task
import com.personaltasks.model.TaskDAO
import com.personaltasks.model.TaskFirebaseDatabase
import com.personaltasks.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainController(private val mainActivity: MainActivity) {
    private val taskDAO: TaskDAO = TaskFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun retrieveActiveTasks() = taskDAO.retrieveTasks().filter { !it.deleted }
    fun insertTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.createTask(task)
        }
    }
    fun getTasks(){
        databaseCoroutineScope.launch {
            val taskList = retrieveActiveTasks()
            mainActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, taskList.toTypedArray())
            })
        }
    }

    fun modifyTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.updateTask(task)
        }
    }
    fun removeTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.deleteTask(task)
        }
    }
}