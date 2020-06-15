package com.kkapps.backpocket.models

import com.kkapps.backpocket.utils.Constants
import com.kkapps.backpocket.utils.Constants.FieldEnum

sealed class FieldCategory(val fieldEnum: FieldEnum) {

    val categorySet: MutableSet<String> = Constants.EVENT_FIELDS

    object Event: FieldCategory(FieldEnum.EVENT)
    object Group: FieldCategory(FieldEnum.GROUP)
    object TxType: FieldCategory(FieldEnum.TxTYPE)
    object TxMode: FieldCategory(FieldEnum.TxMODE)
    object TxCat: FieldCategory(FieldEnum.TxCAT)
    object TxResult: FieldCategory(FieldEnum.TxRESULT)
    object Social: FieldCategory(FieldEnum.SOCIAL)
    object Feeling: FieldCategory(FieldEnum.FEELING)
    object Relation: FieldCategory(FieldEnum.RELATION)
    object Unknown: FieldCategory(FieldEnum.UNKNOWN)

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