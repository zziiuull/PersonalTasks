package com.personaltasks.adapter

import android.annotation.SuppressLint
import com.personaltasks.R
import com.personaltasks.databinding.TileTaskBinding
import com.personaltasks.model.Task
import com.personaltasks.ui.OnTaskClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

// test
class TaskRvAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
): RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(ttb: TileTaskBinding): RecyclerView.ViewHolder(ttb.root){
        val titleTv: TextView = ttb.titleTv
        val descriptionTv: TextView = ttb.descriptionTv
        val duedateTv: TextView = ttb.duedateTv
        val isdoneTv: TextView = ttb.isdoneTv
        val priority: TextView = ttb.priorityTv

        init {
            ttb.root.setOnCreateContextMenuListener{ menu, v, menuInfo ->
                (onTaskClickListener as AppCompatActivity).menuInflater.inflate(R.menu.context_menu, menu)
                menu.findItem(R.id.edit_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onEditTaskMenuItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.remove_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onRemoveTaskMenuItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.details_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onTaskClick(adapterPosition)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder = TaskViewHolder(
        TileTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        taskList[position].let { task ->
            with(holder) {
                var priorityStr = "Baixa"
                if (task.priority == 2) priorityStr = "Média"
                else if (task.priority == 3) priorityStr = "Alta"

                titleTv.text = task.title
                descriptionTv.text = task.description
                duedateTv.text = task.dueDate
                isdoneTv.text = if (task.isDone) "Done" else "Not done"
                priority.text = "Priority: " + priorityStr
            }
        }
    }

    override fun getItemCount(): Int = taskList.size
}