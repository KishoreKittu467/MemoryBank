package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Task(
    @ColumnInfo(name = "content") var content: CharSequence,
    @ColumnInfo(name = "subTasks") var subTasks: MutableSet<Task>? = null
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: String = ""

    @ColumnInfo(name = "isCompleted") private var isCompleted: Boolean = false

    fun addSubTask(task: Task) {
        if(subTasks.isNullOrEmpty()) {
            subTasks = mutableSetOf()
        }
        subTasks?.add(task)
        calculateStatus()
    }

    fun removeSubTask(task: Task) {
        subTasks?.removeAll { it.id == task.id }
        calculateStatus()
    }

    fun setCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    private fun calculateStatus() {
        subTasks?.apply {
            isCompleted = all{ task ->  task.isCompleted }
        }
    }
}