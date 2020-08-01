package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Recursion (
    @ColumnInfo(name = "start_time") var startTime: Long = 0,
    @ColumnInfo(name = "end_time") var endTime: Long = 0,
    @ColumnInfo(name = "count") var count: Int = 0,
    @ColumnInfo(name = "frequency") var frequency: Long = 0
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: String = ""
}
