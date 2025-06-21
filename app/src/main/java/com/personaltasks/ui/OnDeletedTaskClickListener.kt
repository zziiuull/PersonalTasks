package com.personaltasks.ui

interface OnDeletedTaskClickListener {
    fun onDeletedTaskClick(position: Int);
    fun onDeletedTaskRestoreClick(position: Int)
}