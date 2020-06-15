package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.kkapps.backpocket.utils.Constants

data class Person(

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Dp? = null,

    @ColumnInfo(name = "names") var names: MutableSet<Type>? = null, // setOf(Type("firstName", "Kishore"), Type("lastName", "Kittu"))
    @ColumnInfo(name = "phones") var phones: MutableSet<Type>? = null, // setOf(Type("Mobile", "+918106644656"), Type("Home", "8106644656")
    @ColumnInfo(name = "mailIds") var mailIds: MutableSet<Type>? = null, // setOf(Type("Office", "msk@tw.com"), Type("Personal", "msk@gm.com")
    @ColumnInfo(name = "dob") var dob: Long = 0,
    @ColumnInfo(name = "gender") var gender: Constants.GENDER = Constants.GENDER.MALE,
    @ColumnInfo(name = "gallery") var gallery: Set<Dp>? = null,
    @ColumnInfo(name = "relation") var relation: Tag,
    @ColumnInfo(name = "isMarked") var isMarked: Boolean = false,
    @ColumnInfo(name = "links") var links: MutableSet<FieldType>? = null,
    @ColumnInfo(name = "directLoan") var directLoan: Float = 0F,
    @ColumnInfo(name = "inDirectLoan") var inDirectLoan: Float = 0F,
    @ColumnInfo(name = "group") var group: FieldType? = null
) //: Type
{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: String = ""
}

fun Person.setDirectLoan(loan: Float) {
    directLoan = loan
}