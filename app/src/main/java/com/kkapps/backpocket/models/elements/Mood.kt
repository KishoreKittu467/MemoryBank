package com.kkapps.backpocket.models.elements

import androidx.room.ColumnInfo
import com.kkapps.backpocket.models.FieldType

data class Mood (

    @ColumnInfo(name = "feeling") var feeling: FieldType

//    @ColumnInfo(name = "entryType") var eventType: FieldType,
//    @ColumnInfo(name = "created_time") var createdTime: Long = 0,
//    @ColumnInfo(name = "start_time") var startTime: Long = 0,
//    @ColumnInfo(name = "last_modified") var lastModified: Long = 0,
//    @ColumnInfo(name = "title") var title: String = "",
//    @ColumnInfo(name = "isMarked") var readOnly: Boolean = false,
//    @ColumnInfo(name = "color") var color: Int = 0,
//    @ColumnInfo(name = "text") var text: CharSequence? = "",
//    @ColumnInfo(name = "gallery") var gallery: Set<Dp>? = null,
//    @ColumnInfo(name = "repeat") var repeat: List<Recursion>? = null,
//    @ColumnInfo(name = "tags") var tags: MutableSet<Type>? = null,
//    @ColumnInfo(name = "people") var people: MutableSet<Type>? = null,
//    @ColumnInfo(name = "contributors") var contributors: MutableSet<Type>? = null,
//    @ColumnInfo(name = "ownerId") var ownerId: String = "12JH1A0467",

//    @ColumnInfo(name = "name") var name: String,
//    @ColumnInfo(name = "note") var note: String? = null,
//    @ColumnInfo(name = "image") var image: Dp? = null
) // : Entry, Type  // isMarked = isAdded ➕