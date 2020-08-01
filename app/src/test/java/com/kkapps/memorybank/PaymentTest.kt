package com.kkapps.memorybank

import com.kkapps.memorybank.home.models.Image
import com.kkapps.memorybank.home.models.FieldType
import com.kkapps.memorybank.home.models.PersonLite
import com.kkapps.memorybank.home.models.elements.Payment
import com.kkapps.memorybank.commons.utils.Constants
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PaymentUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `calculate transaction result`() {
        val personOne = PersonLite(
            name = Constants.MY_NAME,
            image = Image(photo = Constants.MY_DP_1),
            isMarked = Constants.CAN_I_BE_STARRED
        )

        personOne.id = Constants.MY_ID

        val personTwo = PersonLite(
            name = "Raj",
            image = Image(Constants.MY_DP_2),
            directLoan = 50f,
            indirectLoan = -50f
        )
        val transaction = Payment(
            elementType = FieldType(Constants.EVENT_PMNT),

            amount = 100f,
            payer = personOne,
            payee = personTwo,
            txType = FieldType(Constants.TxTYPE_SPENT)
        )
        transaction.calculateResult()
        assertEquals(personTwo.directLoan, 50f)
    }
}