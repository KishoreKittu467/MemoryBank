package com.kkapps.memorybank.models

import androidx.room.ColumnInfo
import com.kkapps.memorybank.utils.Constants
import com.kkapps.memorybank.utils.Constants.Field

data class FieldType(
    @ColumnInfo(name = "field") var field: String = Constants.EVENT_PMNT,
    @ColumnInfo(name = "entity") var entity: Entity? = Entity(field)
) {
    @ColumnInfo(name = "category") val category: Field = FieldCategory.of(field).fieldCategory
}
