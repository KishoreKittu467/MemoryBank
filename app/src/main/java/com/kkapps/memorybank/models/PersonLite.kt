package com.kkapps.memorybank.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class PersonLite(

    @ColumnInfo(name = "name") @PrimaryKey var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null,

    @ColumnInfo(name = "is_marked") var isMarked: Boolean = false,
    @ColumnInfo(name = "direct_oan") var directLoan: Float = 0F,
    @ColumnInfo(name = "indirect_loan") var indirectLoan: Float = 0F
) //: Entity
{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: String = ""
}
