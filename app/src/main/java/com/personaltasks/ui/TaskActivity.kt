package com.personaltasks.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.personaltasks.databinding.ActivityTaskBinding
import com.personaltasks.model.Constant.EXTRA_TASK
import com.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.personaltasks.model.Task
import java.time.LocalDate
import java.util.Calendar

class TaskActivity : AppCompatActivity() {
    private var selectedDate: LocalDate? = null

    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        setSupportActionBar(atb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New contact"

        val calendar = Calendar.getInstance()

        atb.openDialogBt.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val monthCorrected = selectedMonth + 1
                    selectedDate = LocalDate.of(selectedYear, monthCorrected, selectedDayOfMonth)

                    atb.openDialogBt.text = selectedDate.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

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
                atb.openDialogBt.visibility = View.VISIBLE
                atb.dateTv.visibility = View.GONE

                val viewContact = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
                if (viewContact) {
                    supportActionBar?.subtitle = "View task"
                    titleEt.isEnabled = false
                    descriptionEt.isEnabled = false
                    atb.openDialogBt.visibility = View.GONE
                    atb.dateTv.visibility = View.VISIBLE
                    atb.dateTv.text = it.dueDate
                    saveBt.visibility = View.GONE
                }
            }
        }

        with(atb) {
            saveBt.setOnClickListener {
                Task(
                    receivedTask?.id?:hashCode(),
                    titleEt.text.toString(),
                    descriptionEt.text.toString(),
                    selectedDate.toString()

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