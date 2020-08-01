package com.kkapps.memorybank.home.models

import com.kkapps.memorybank.commons.utils.Constants
import com.kkapps.memorybank.commons.utils.Constants.Field

sealed class FieldCategory(val fieldCategory: Field) {

    val categorySet: MutableSet<String> = Constants.EVENT_FIELDS
    val fieldName: Field = Field.EVENT

    object Event: FieldCategory(Field.EVENT)
    object Group: FieldCategory(Field.GROUP)
    object TxType: FieldCategory(Field.TxTYPE)
    object TxMode: FieldCategory(Field.TxMODE)
    object TxCat: FieldCategory(Field.TxCAT)
    object TxResult: FieldCategory(Field.TxRESULT)
    object Social: FieldCategory(Field.SOCIAL)
    object Feeling: FieldCategory(Field.FEELING)
    object Relation: FieldCategory(Field.RELATION)
    object Unknown: FieldCategory(Field.UNKNOWN)

    companion object {
        fun of(fieldName: String): FieldCategory {
            return when {
                Constants.EVENT_FIELDS.contains(fieldName) -> Event
                Constants.GROUP_FIELDS.contains(fieldName) -> Group
                Constants.TxTYPE_FIELDS.contains(fieldName) -> TxType
                Constants.TxMODE_FIELDS.contains(fieldName) -> TxMode
                Constants.TxCAT_FIELDS.contains(fieldName) -> TxCat
                Constants.TxRESULT_FIELDS.contains(fieldName) -> TxResult
                Constants.SOCIAL_FIELDS.contains(fieldName) -> Social
                Constants.FEELING_FIELDS.contains(fieldName) -> Feeling
                Constants.RELATION_FIELDS.contains(fieldName) -> Relation
                else -> Unknown
            }
        }
    }
}
