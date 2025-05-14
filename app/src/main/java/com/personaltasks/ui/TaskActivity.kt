package com.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.personaltasks.databinding.ActivityTaskBinding
import com.personaltasks.model.Constant.EXTRA_TASK
import com.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.personaltasks.model.Task

class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        setSupportActionBar(atb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New contact"

        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        }
        else {
            intent.getParcelableExtra<Task>(EXTRA_TASK)
        }
        receivedTask?.let{
            supportActionBar?.subtitle = "Edit task"
            with(atb) {
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)

                val viewContact = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
                if (viewContact) {
                    supportActionBar?.subtitle = "View task"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    saveBt.visibility = View.GONE
                }
            }
        }

        with(atb) {
            saveBt.setOnClickListener {
                Task(
                    receivedTask?.id?:hashCode(),
                    titleEt.text.toString(),
                    descriptionEt.text.toString()
                ).let { contact ->
                    Intent().apply {
                        putExtra(EXTRA_TASK, contact)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }
        }
    }
}