package com.kkapps.backpocket.models

import androidx.room.ColumnInfo
import com.kkapps.backpocket.utils.Constants
import com.kkapps.backpocket.utils.Constants.FieldEnum

data class FieldType(
    @ColumnInfo(name = "fieldName") var fieldName: String = Constants.EVENT_TR,
    @ColumnInfo(name = "type") var type: Type? = Type(fieldName)
) {
    @ColumnInfo(name = "fieldCategory") val fieldCategory: FieldEnum = FieldCategory.of(fieldName).fieldEnum
}
