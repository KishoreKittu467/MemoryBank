package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.kkapps.backpocket.utils.Constants

data class Entry(
    @ColumnInfo(name = "entryType") var eventType: FieldType,
    @ColumnInfo(name = "created_time") var createdTime: Long = 0,
    @ColumnInfo(name = "start_time") var startTime: Long = 0,
    @ColumnInfo(name = "last_modified") var lastModified: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "isMarked") var readOnly: Boolean = false,
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "text") var text: CharSequence? = "",
    @ColumnInfo(name = "gallery") var gallery: Set<Dp>? = null,
    @ColumnInfo(name = "repeat") var repeat: List<Recursion>? = null,
    @ColumnInfo(name = "tags") var tags: MutableSet<Type>? = null, /** setOf(Type("#follow"), Type("#followMe")) */
    @ColumnInfo(name = "people") var people: MutableSet<Type>? = null, /** setOf(Type(personOneId, personOneName, Dp(Constants.MY_DP_1)), Type(Type(personTwoId, personTwoName, Dp(Constants.MY_DP_2)) */
    @ColumnInfo(name = "contributors") var contributors: MutableSet<Type>? = null, /** setOf(Type(personOneId, personOneName, Dp(Constants.MY_DP_1)), Type(Type(personTwoId, personTwoName, Dp(Constants.MY_DP_2)) */
    @ColumnInfo(name = "ownerId") var ownerId: String = Constants.MY_ID
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: String = "$eventType $createdTime"
}