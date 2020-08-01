package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class Image(
    @ColumnInfo(name = "photo") var photo: String? = null,
    @ColumnInfo(name = "icon") var icon: Int = 0,
    @ColumnInfo(name = "color") var color: Int = 0
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: String = photo.orEmpty()
}
