package com.personaltasks.controller

import android.os.Message
import com.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.personaltasks.model.Task
import com.personaltasks.model.TaskDAO
import com.personaltasks.model.TaskFirebaseDatabase
import com.personaltasks.ui.DeletedTaskActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeletedTaskController(private val deletedTaskActivity: DeletedTaskActivity) {
    private val taskDAO: TaskDAO = TaskFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun retrieveDeletedTasks() = taskDAO.retrieveTasks().filter { it.deleted }
    fun restoreTask(task: Task): Int {
        val restoredTask = task.copy(deleted = false)
        return taskDAO.updateTask(restoredTask)
    }
    fun getDeletedTasks(){
        databaseCoroutineScope.launch {
            val taskList = retrieveDeletedTasks()
            deletedTaskActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, taskList.toTypedArray())
            })
        }
    }
}