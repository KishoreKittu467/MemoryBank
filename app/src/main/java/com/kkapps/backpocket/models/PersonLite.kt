package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class PersonLite(

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: Dp? = null,

    @ColumnInfo(name = "isMarked") var isMarked: Boolean = false,
    @ColumnInfo(name = "directLoan") var directLoan: Float = 0F,
    @ColumnInfo(name = "inDirectLoan") var indirectLoan: Float = 0F
) //: Type
{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: String = ""
}
