package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo
import com.kkapps.memorybank.home.utils.Constants
import com.kkapps.memorybank.home.utils.Constants.Field

data class FieldType(
    @ColumnInfo(name = "field") var field: String = Constants.EVENT_PMNT,
    @ColumnInfo(name = "entity") var entity: Entity? = Entity(field)
) {
    @ColumnInfo(name = "category") val category: Field = FieldCategory.of(field).fieldCategory
}
