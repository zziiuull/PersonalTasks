package com.personaltasks.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Task(
    val id: Int,
    var title: String = "",
    var description: String = ""
): Parcelable