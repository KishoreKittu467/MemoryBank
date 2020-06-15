package com.kkapps.backpocket.models

import androidx.room.ColumnInfo

data class Event(
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "entryType") var eventType: FieldType,
    @ColumnInfo(name = "desc") var desc: CharSequence? = null
)