package com.kkapps.backpocket.models.elements

import androidx.room.ColumnInfo
import com.kkapps.backpocket.models.Dp
import com.kkapps.backpocket.models.FieldType
import com.kkapps.backpocket.models.PersonLite
import com.kkapps.backpocket.utils.Constants

class Transaction(
    @ColumnInfo(name = "amount") var amount: Float = 0F,
    @ColumnInfo(name = "payer") var payer: PersonLite = PersonLite(Constants.MY_NAME, Dp(Constants.MY_DP_1), Constants.CAN_I_BE_STARRED),
    @ColumnInfo(name = "payee") var payee: PersonLite? = null, // default = none, Type(personId, personName, Dp(Constants.MY_DP_1))
    @ColumnInfo(name = "txType") var txType: FieldType = FieldType(Constants.TxTYPE_PAID),
    @ColumnInfo(name = "source") var source: FieldType? = FieldType(Constants.TxMODE_CASH),
    @ColumnInfo(name = "category") var category: FieldType? = FieldType(Constants.TxCAT_FOOD)

//    @ColumnInfo(name = "feeling") var feeling: FieldType,

//    @ColumnInfo(name = "entryType") var eventType: FieldType,
//    @ColumnInfo(name = "created_time") var createdTime: Long = 0,
//    @ColumnInfo(name = "start_time") var startTime: Long = 0,
//    @ColumnInfo(name = "last_modified") var lastModified: Long = 0,
//    @ColumnInfo(name = "title") var title: String = "",
//    @ColumnInfo(name = "isMarked") var readOnly: Boolean = false,
//    @ColumnInfo(name = "color") var color: Int = 0,
//    @ColumnInfo(name = "text") var text: CharSequence? = "",
//    @ColumnInfo(name = "gallery") var gallery: Set<Dp>? = null,
//    @ColumnInfo(name = "repeat") var repeat: List<Recursion>? = null,
//    @ColumnInfo(name = "tags") var tags: MutableSet<Type>? = null,
//    @ColumnInfo(name = "people") var people: MutableSet<Type>? = null,
//    @ColumnInfo(name = "contributors") var contributors: MutableSet<Type>? = null,
//    @ColumnInfo(name = "ownerId") var ownerId: String = "12JH1A0467",

//    @ColumnInfo(name = "name") var name: String,
//    @ColumnInfo(name = "note") var note: String? = null,
//    @ColumnInfo(name = "image") var image: Dp? = null
) // Mood  // isMarked = isImp ⚡️
{
    @ColumnInfo(name = "result")
    private var result: FieldType? = FieldType(Constants.TxRESULT_CLEAR)

    fun calculateResult() {
        val oldResult = result?.fieldName ?: Constants.TxRESULT_CLEAR
        var newDirectLoan = 0f
        var newIndirectLoan = 0f
        if (payer.id == Constants.MY_ID) {
            payee.let {
                result = FieldType(
                    if (it != null) {
                        val directSum = amount + it.directLoan
                        val indirectSum = amount + it.indirectLoan
                        when (txType.fieldName) {
                            Constants.TxTYPE_PAID -> {
                                Constants.TxRESULT_EXPENSE
                            }
                            Constants.TxTYPE_GAVE -> {
                                newIndirectLoan = it.indirectLoan
                                if (it.directLoan < 0) {
                                    when {
                                        directSum > 0 -> {
                                            newDirectLoan +=  directSum // + (amount + it.directLoan)
                                            Constants.TxRESULT_ASSET
                                        }
                                        directSum < 0 -> {
                                            newDirectLoan -= directSum // - (amount + it.directLoan)
                                            Constants.TxRESULT_LIABILITY
                                        }
                                        else -> {
                                            newDirectLoan = 0f
                                            Constants.TxRESULT_CLEAR
                                        }
                                    }
                                } else {
                                    newDirectLoan += directSum // + (amount + it.directLoan)
                                    Constants.TxRESULT_ASSET
                                }
                            }
                            Constants.TxTYPE_SPENT -> {
                                newDirectLoan = it.directLoan
                                if (it.indirectLoan < 0) {
                                    when {
                                        indirectSum > 0 -> {
                                            newIndirectLoan +=  indirectSum // + (amount + it.indirectLoan)
                                            Constants.TxRESULT_OUTFLOW
                                        }
                                        indirectSum < 0 -> {
                                            newIndirectLoan -=  indirectSum // - (amount + it.indirectLoan)
                                            Constants.TxRESULT_INFLOW
                                        }
                                        else -> {
                                            newIndirectLoan = 0f
                                            Constants.TxRESULT_CLEAR
                                        }
                                    }
                                } else {
                                    newIndirectLoan +=  indirectSum // + (amount + it.indirectLoan)
                                    Constants.TxRESULT_OUTFLOW
                                }
                            }
                            else -> oldResult
                        }
                    } else {
                        if (txType.fieldName == Constants.TxTYPE_PAID) {
                            Constants.TxRESULT_EXPENSE
                        } else {
                            //payer not set & txType is SPENT/GAVE
                            Constants.TxRESULT_MISC
                        }
                    }
                )
                updateLoanValues(oldResult, it, newDirectLoan, newIndirectLoan)
            }
        } else {
            payer.let {
                result = FieldType(
                    if (payee != null) {
                        val directDiff = amount - it.directLoan
                        val indirectDiff = amount - it.indirectLoan
                        when (txType.fieldName) {
                            Constants.TxTYPE_PAID -> {
                                Constants.TxRESULT_INCOME
                            }
                            Constants.TxTYPE_GAVE -> {
                                newIndirectLoan = it.indirectLoan
                                if (it.directLoan > 0) {
                                    when {
                                        directDiff > 0 -> {
                                            newDirectLoan -= directDiff // - (amount - it.directLoan)
                                            Constants.TxRESULT_LIABILITY
                                        }
                                        directDiff < 0 -> {
                                            newDirectLoan +=  directDiff // + (amount - it.directLoan)
                                            Constants.TxRESULT_ASSET
                                        }
                                        else -> {
                                            newDirectLoan = 0f
                                            Constants.TxRESULT_CLEAR
                                        }
                                    }
                                } else {
                                    newDirectLoan -= directDiff // - (amount + it.directLoan)
                                    Constants.TxRESULT_LIABILITY
                                }
                            }
                            Constants.TxTYPE_SPENT -> {
                                newDirectLoan = it.directLoan
                                if (it.indirectLoan > 0) {
                                    when {
                                        indirectDiff > 0 -> {
                                            newIndirectLoan -= indirectDiff // - (amount - it.indirectLoan)
                                            Constants.TxRESULT_INFLOW
                                        }
                                        indirectDiff < 0 -> {
                                            newIndirectLoan += indirectDiff // + (amount - it.indirectLoan)
                                            Constants.TxRESULT_OUTFLOW
                                        }
                                        else -> {
                                            newIndirectLoan = 0f
                                            Constants.TxRESULT_CLEAR
                                        }
                                    }
                                } else {
                                    newIndirectLoan -= indirectDiff // - (amount - it.indirectLoan)
                                    Constants.TxRESULT_INFLOW
                                }
                            }
                            else -> oldResult
                        }
                    } else {
                        if (txType.fieldName == Constants.TxTYPE_PAID) {
                            Constants.TxRESULT_INCOME
                        } else {
                            //payee not set & txType is SPENT / GAVE
                            Constants.TxRESULT_MISC
                        }
                    }
                )
                updateLoanValues(oldResult, it, newDirectLoan, newIndirectLoan)
            }
        }
    }

    private fun updateLoanValues(oldResult: String, person: PersonLite?, direct: Float, indirect: Float) {
        person?.apply {
            val newResult = result?.fieldName ?: Constants.TxRESULT_CLEAR
            if (oldResult != newResult) {
                directLoan = direct
                indirectLoan = indirect
            }
        }
    }
}