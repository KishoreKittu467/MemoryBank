package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Entity(
    @ColumnInfo(name = "name") @PrimaryKey var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null
)
