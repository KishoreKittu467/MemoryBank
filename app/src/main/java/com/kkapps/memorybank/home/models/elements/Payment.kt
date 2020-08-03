package com.kkapps.memorybank.home.models.elements

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.kkapps.memorybank.home.models.FieldType
import com.kkapps.memorybank.home.models.Image
import com.kkapps.memorybank.home.models.Recursion
import com.kkapps.memorybank.home.models.PersonLite
import com.kkapps.memorybank.home.models.Entity
import com.kkapps.memorybank.home.utils.Constants

class Payment(

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

    @ColumnInfo(name = "name") var name: String = "",/** initialised with empty string */
    @ColumnInfo(name = "notes") var notes: String? = null,
    @ColumnInfo(name = "image") var image: Image? = null,

    @ColumnInfo(name = "feeling") var feeling: FieldType = FieldType(Constants.FEELING_HPY),

    @ColumnInfo(name = "amount") var amount: Float = 0F,
    @ColumnInfo(name = "payer") var payer: PersonLite = PersonLite(name = Constants.MY_NAME, image = Image(
        Constants.MY_DP_1), isMarked = Constants.CAN_I_BE_STARRED),
    @ColumnInfo(name = "payee") var payee: PersonLite? = null, // default = none, Entity(personId, personName, Image(Constants.MY_DP_1))
    @ColumnInfo(name = "tx_type") var txType: FieldType = FieldType(Constants.TxTYPE_PAID),
    @ColumnInfo(name = "source") var source: FieldType? = FieldType(Constants.TxMODE_CASH),
    @ColumnInfo(name = "tx_category") var txCategory: FieldType? = null
) // Mood  // isMarked = isImp ⚡️
{
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: String = "$elementType $createdTime"

    @ColumnInfo(name = "result") private var result: FieldType? = FieldType(Constants.TxRESULT_CLEAR)

    fun calculateResult() {
        val oldResult = result?.field ?: Constants.TxRESULT_CLEAR
        var newDirectLoan = 0f
        var newIndirectLoan = 0f
        if (payer.id == Constants.MY_ID) {
            payee.let {
                result = FieldType(
                    if (it != null) {
                        val directSum = amount + it.directLoan
                        val indirectSum = amount + it.indirectLoan
                        when (txType.field) {
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
                        if (txType.field == Constants.TxTYPE_PAID) {
                            Constants.TxRESULT_EXPENSE
                        } else { //payer not set & txType is SPENT/GAVE
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
                        when (txType.field) {
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
                        if (txType.field == Constants.TxTYPE_PAID) {
                            Constants.TxRESULT_INCOME
                        } else { //payee not set & txType is SPENT / GAVE
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
            val newResult = result?.field ?: Constants.TxRESULT_CLEAR
            if (oldResult != newResult) {
                directLoan = direct
                indirectLoan = indirect
            }
        }
    }
}
