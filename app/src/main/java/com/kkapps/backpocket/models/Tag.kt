package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Tag(

//    @ColumnInfo(name = "name") @PrimaryKey var name: String,
//    @ColumnInfo(name = "image") var image: Dp? = null
    @ColumnInfo(name = "relTags") var relTags: MutableSet<Task>? = null,
    @ColumnInfo(name = "relCats") var relCats: MutableSet<Task>? = null
) //: Type