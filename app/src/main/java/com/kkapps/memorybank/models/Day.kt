package com.kkapps.memorybank.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Day(
    @ColumnInfo(name = "date_time") @PrimaryKey var dateTime: Long = 0,
    @ColumnInfo(name = "events") var events: MutableSet<Event>? = null
)