package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Type(
    @ColumnInfo(name = "name") @PrimaryKey var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Dp? = null
)