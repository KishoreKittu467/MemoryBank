package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.kkapps.memorybank.commons.utils.Constants

data class Person(

    @ColumnInfo(name = "names") var names: MutableSet<Entity>? = null, // setOf(Entity("firstName", "Kishore"), Entity("lastName", "Kittu"))
    @ColumnInfo(name = "phone_numbers") var phoneNumbers: MutableSet<Entity>? = null, // setOf(Entity("Mobile", "+918106644656"), Entity("Home", "8106644656")
    @ColumnInfo(name = "emails") var emails: MutableSet<Entity>? = null, // setOf(Entity("Office", "msk@tw.com"), Entity("Personal", "msk@gm.com")
    @ColumnInfo(name = "dob") var dob: Long = 0,
    @ColumnInfo(name = "gender") var gender: Constants.GENDER = Constants.GENDER.MALE,
    @ColumnInfo(name = "gallery") var gallery: Set<Image>? = null,
    @ColumnInfo(name = "relation") var relation: Entity? = null,/** Entity("#tagName") */
    @ColumnInfo(name = "is_marked") var isMarked: Boolean = false,
    @ColumnInfo(name = "links") var links: MutableSet<FieldType>? = null,
    @ColumnInfo(name = "groups") var group: MutableSet<FieldType>? = null,
    @ColumnInfo(name = "direct_loan") var directLoan: Float = 0F,
    @ColumnInfo(name = "indirect_loan") var inDirectLoan: Float = 0F,

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null
) //: Entity
{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: String = ""
}

