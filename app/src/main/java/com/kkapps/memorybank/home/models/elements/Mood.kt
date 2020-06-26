package com.kkapps.memorybank.home.models.elements

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.kkapps.memorybank.home.models.FieldType
import com.kkapps.memorybank.home.models.Image
import com.kkapps.memorybank.home.models.Recursion
import com.kkapps.memorybank.home.models.Entity
import com.kkapps.memorybank.commons.utils.Constants

data class Mood (

    @ColumnInfo(name = "element_type") var elementType: FieldType,
    @ColumnInfo(name = "created_time") var createdTime: Long = 0,
    @ColumnInfo(name = "start_time") var startTime: Long = 0,
    @ColumnInfo(name = "last_modified") var lastModified: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "is_marked") var isMarked: Boolean = false,
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "text") var text: CharSequence? = "",
    @ColumnInfo(name = "gallery") var gallery: Set<Image>? = null,
    @ColumnInfo(name = "repeat") var repeat: List<Recursion>? = null,
    @ColumnInfo(name = "tags") var tags: MutableSet<Entity>? = null,/** setOf(Entity("#follow"), Entity("#followMe")) */
    @ColumnInfo(name = "people") var people: MutableSet<Entity>? = null,/** setOf(Entity(personOneId, personOneName, Image(Constants.MY_DP_1)), Entity(Entity(personTwoId, personTwoName, Image(Constants.MY_DP_2)) */
    @ColumnInfo(name = "contributors") var contributors: MutableSet<Entity>? = null,/** setOf(Entity(personOneId, personOneName, Image(Constants.MY_DP_1)), Entity(Entity(personTwoId, personTwoName, Image(Constants.MY_DP_2)) */
    @ColumnInfo(name = "ownerId") var ownerId: String = Constants.MY_ID,

    @ColumnInfo(name = "name") var name: String = Constants.FEELING_HPY,
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null,

    @ColumnInfo(name = "feeling") var feeling: FieldType = FieldType(name)
) // : Element, Entity  // isMarked = isAdded ➕
{
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: String = "$elementType $createdTime"
}