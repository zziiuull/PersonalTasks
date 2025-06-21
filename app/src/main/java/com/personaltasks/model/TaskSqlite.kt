package com.personaltasks.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.personaltasks.R
import java.sql.SQLException

class TaskSqlite(context: Context): TaskDAO {
    companion object {
        private val TASK_DATABSE_FILE = "taskList"
        private val TASK_TABLE = "task"
        private val ID_COLUMN = "id"
        private val TITLE_COLUMN = "title"
        private val DESCRIPTION_COLUMN = "descriptions"
        private val DUEDATE_COLUMN = "dueDate"
        private val ISDONE_COLUMN = "isDone"
        private val DELETED_COLUMN = "deleted"


        val CREATE_TASK_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS ${TASK_TABLE} (" +
                "${ID_COLUMN} INTEGER NOT NULL PRIMARY KEY," +
                "${TITLE_COLUMN} TEXT NOT NULL," +
                "${DESCRIPTION_COLUMN} TEXT NOT NULL," +
                "${DUEDATE_COLUMN} TEXT NOT NULL," +
                "${ISDONE_COLUMN} INTEGER NOT NULL DEFAULT 0," +
                "${DELETED_COLUMN} INTEGER NOT NULL DEFAULT 0);"
    }

    private val taskDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        TASK_DATABSE_FILE,
        MODE_PRIVATE,
        null)

    init {
        try {
            taskDatabase.execSQL(CREATE_TASK_TABLE_STATEMENT)
        }
        catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.message.toString())
        }
    }


    override fun createTask(task: Task) =
        taskDatabase.insert(TASK_TABLE, null, task.toContentValues())

    override fun retrieveTask(id: Int): Task {
        val cursor = taskDatabase.query(
            true,
            TASK_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursor.toTask()
        } else {
            Task()
        }
    }

    override fun retrieveTasks(): MutableList<Task> {
        val taskList: MutableList<Task> = mutableListOf()
        val cursor = taskDatabase.rawQuery("SELECT * FROM $TASK_TABLE WHERE $DELETED_COLUMN = 0", null)

        while (cursor.moveToNext()){
            taskList.add(cursor.toTask())
        }

        return taskList
    }

    fun retrieveDeletedTasks(): MutableList<Task> {
        val taskList: MutableList<Task> = mutableListOf()
        val cursor = taskDatabase.rawQuery("SELECT * FROM $TASK_TABLE WHERE $DELETED_COLUMN = 1", null)

        while (cursor.moveToNext()){
            taskList.add(cursor.toTask())
        }

        return taskList
    }

    override fun updateTask(task: Task) = taskDatabase.update(
        TASK_TABLE,
        task.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(task.id.toString())
    )

    override fun deleteTask(task: Task) = taskDatabase.update(
        TASK_TABLE,
        ContentValues().apply { put(DELETED_COLUMN, 1) },
        "$ID_COLUMN = ?",
        arrayOf(task.id.toString())
    )

    fun restoreTask(task: Task) = taskDatabase.update(
        TASK_TABLE,
        ContentValues().apply { put(DELETED_COLUMN, 0) },
        "$ID_COLUMN = ?",
        arrayOf(task.id.toString())
    )

    private fun Task.toContentValues(): ContentValues {
        return ContentValues().apply {
            put(ID_COLUMN, id)
            put(TITLE_COLUMN, title)
            put(DESCRIPTION_COLUMN, description)
            put(DUEDATE_COLUMN, dueDate)
            put(ISDONE_COLUMN, if (isDone) 1 else 0)
            put(DELETED_COLUMN, if (deleted) 1 else 0)
        }
    }

    private fun Cursor.toTask() = Task(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(TITLE_COLUMN)),
        getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
        getString(getColumnIndexOrThrow(DUEDATE_COLUMN)),
        getInt(getColumnIndexOrThrow(ISDONE_COLUMN)) == 1,
        getInt(getColumnIndexOrThrow(DELETED_COLUMN)) == 1
    )
}