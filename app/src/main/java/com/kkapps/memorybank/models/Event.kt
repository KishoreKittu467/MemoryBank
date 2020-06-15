package com.kkapps.memorybank.models

import androidx.room.ColumnInfo

data class Event(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "entry_type") var eventType: FieldType,
    @ColumnInfo(name = "desc") var desc: CharSequence? = null
)