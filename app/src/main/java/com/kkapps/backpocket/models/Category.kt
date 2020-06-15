package com.kkapps.backpocket.models

import androidx.room.ColumnInfo

data class Category(

//    @ColumnInfo(name = "name") @PrimaryKey var name: String,
//    @ColumnInfo(name = "image") var image: Dp? = null,

//    @ColumnInfo(name = "relTags") var relTags: MutableSet<Task>? = null,
//    @ColumnInfo(name = "relCats") var relCats: MutableSet<Task>? = null

    @ColumnInfo(name = "subCats") var subCats: MutableList<Task>? = null
) //: Tag