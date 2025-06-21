package com.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.personaltasks.R
import com.personaltasks.databinding.TileTaskBinding
import com.personaltasks.model.Task
import com.personaltasks.ui.OnDeletedTaskClickListener

class DeletedTaskRvAdapter(
    private val taskList: MutableList<Task>,
    private val onDeletedTaskClickListener: OnDeletedTaskClickListener
): RecyclerView.Adapter<DeletedTaskRvAdapter.DeletedTaskViewHolder>() {
    inner class DeletedTaskViewHolder(ttb: TileTaskBinding): RecyclerView.ViewHolder(ttb.root){
        val titleTv: TextView = ttb.titleTv
        val descriptionTv: TextView = ttb.descriptionTv
        val duedateTv: TextView = ttb.duedateTv

        init {
            ttb.root.setOnCreateContextMenuListener { menu, v, menuInfo ->
                (onDeletedTaskClickListener as AppCompatActivity).menuInflater.inflate(R.menu.context_menu_deleted, menu)
                menu.findItem(R.id.context_restore_task_mi).setOnMenuItemClickListener {
                    onDeletedTaskClickListener.onDeletedTaskRestoreClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.details_mi).setOnMenuItemClickListener {
                    onDeletedTaskClickListener.onDeletedTaskClick(adapterPosition)
                    true
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeletedTaskViewHolder = DeletedTaskViewHolder(
        TileTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(
        holder: DeletedTaskViewHolder,
        position: Int
    ) {
        taskList[position].let { task ->
            with(holder){
                titleTv.text = task.title
                descriptionTv.text = task.description
                duedateTv.text = task.dueDate
            }
        }
    }
}