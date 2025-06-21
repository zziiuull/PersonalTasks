package com.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.personaltasks.adapter.DeletedTaskRvAdapter
import com.personaltasks.controller.DeletedTaskController
import com.personaltasks.databinding.ActivityDeletedBinding
import com.personaltasks.model.Constant.EXTRA_TASK
import com.personaltasks.model.Constant.EXTRA_TASK_ARRAY
import com.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.personaltasks.model.Task

class DeletedTaskActivity: AppCompatActivity(), OnDeletedTaskClickListener {
    private val adb: ActivityDeletedBinding by lazy {
        ActivityDeletedBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val deletedTaskAdapter: DeletedTaskRvAdapter by lazy {
        DeletedTaskRvAdapter(taskList, this)
    }

    private val deleteTaskController: DeletedTaskController by lazy {
        DeletedTaskController(this)
    }

    companion object {
        const val GET_TASKS_MESSAGE = 1
        const val GET_CONTACTS_INTERVAL = 2000L
    }

    val getTasksHandler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GET_TASKS_MESSAGE){
                deleteTaskController.getDeletedTasks()
                sendMessageDelayed(obtainMessage().apply { what = GET_TASKS_MESSAGE }, GET_CONTACTS_INTERVAL)

            }
            else {
                val contactArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY, Task::class.java)
                }
                else {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY)
                }
                taskList.clear()
                contactArray?.forEach { taskList.add(it as Task) }
                deletedTaskAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adb.root)

        setSupportActionBar(adb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Deleted tasks"

        adb.deletedRv.adapter = deletedTaskAdapter
        adb.deletedRv.layoutManager = LinearLayoutManager(this)

        getTasksHandler.sendMessageDelayed(Message().apply { what = GET_TASKS_MESSAGE}, GET_CONTACTS_INTERVAL)
    }

    override fun onDeletedTaskClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            putExtra(EXTRA_VIEW_TASK, true)
            startActivity(this)
        }
    }

    override fun onDeletedTaskRestoreClick(position: Int) {
        deleteTaskController.restoreTask(taskList[position])
        taskList.removeAt(position)
        deletedTaskAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Task restored!", Toast.LENGTH_SHORT).show()
    }
}