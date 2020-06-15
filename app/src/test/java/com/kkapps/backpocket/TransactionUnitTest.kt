package com.kkapps.backpocket

import com.kkapps.backpocket.models.Dp
import com.kkapps.backpocket.models.FieldType
import com.kkapps.backpocket.models.PersonLite
import com.kkapps.backpocket.models.elements.Transaction
import com.kkapps.backpocket.utils.Constants
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TransactionUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `calculate transaction result`() {
        val personOne = PersonLite(
            name = Constants.MY_NAME,
            image = Dp(photo = Constants.MY_DP_1),
            isMarked = Constants.CAN_I_BE_STARRED
        )

        personOne.id = Constants.MY_ID

        val personTwo = PersonLite(
            name = "Raj",
            image = Dp(Constants.MY_DP_2),
            isMarked = false,
            directLoan = 50f,
            indirectLoan = -50f
        )
        val transaction = Transaction(
            amount = 100f,
            payer = personOne,
            payee = personTwo,
            txType = FieldType(Constants.TxTYPE_SPENT)
        )
        transaction.calculateResult()
        assertEquals(personTwo.directLoan, 50f)
    }
}