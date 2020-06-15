package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo

data class ElementalEvent(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "entry_type") var eventType: FieldType,
    @ColumnInfo(name = "desc") var desc: CharSequence? = null
)