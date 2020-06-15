package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Tag(

    @ColumnInfo(name = "name") @PrimaryKey var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null,

    @ColumnInfo(name = "rel_cats") var relCats: MutableSet<Entity>? = null,/** setOf(Entity(categoryOneId, categoryOneName, Image(icon = R.drawable.ic_launcher_background)), Entity(categoryTwoId, categoryTwoName, Image(icon = R.drawable.ic_launcher_foreground)) */
    @ColumnInfo(name = "rel_tags") var relTags: MutableSet<Entity>? = null /** setOf(Entity("#tagName1"), Entity("#tagName2")) */
) //: Entity