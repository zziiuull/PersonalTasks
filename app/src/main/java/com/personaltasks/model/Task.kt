package com.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personaltasks.model.Constant.INVALID_TASK_ID
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = INVALID_TASK_ID,
    var title: String = "",
    var description: String = "",
    var dueDate: String? = "",
    var isDone: Boolean = false,
    var deleted: Boolean = false,
    var priority: Int = 1
): Parcelable