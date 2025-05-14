package com.personaltasks.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.personaltasks.R
import com.personaltasks.databinding.TileTaskBinding
import com.personaltasks.model.Task

class TaskAdapter(context: Context, private val taskList: MutableList<Task>) :
    ArrayAdapter<Task>(context, R.layout.tile_task, taskList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = taskList[position]
        var taskTileView = convertView
        if (taskTileView == null){
            taskTileView = TileTaskBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            ).root
        }

        val viewHolder = taskTileView.tag as TileTaskBinding
        viewHolder.titleTv.text = task.title
        viewHolder.descriptionTv.text = task.description
//        viewHolder.duedateTv.text = task.duedate
        return taskTileView
    }

//    private data class TileTaskViewHolder(val titleTv: TextView, val descriptionTv: TextView, val dueDate: TextView)
    private data class TileTaskViewHolder(val titleTv: TextView, val descriptionTv: TextView)
}
