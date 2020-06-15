package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Day(
    @ColumnInfo(name = "dateTime") @PrimaryKey var dateTime: Long = 0,
    @ColumnInfo(name = "events") var events: MutableSet<Event>? = null
)