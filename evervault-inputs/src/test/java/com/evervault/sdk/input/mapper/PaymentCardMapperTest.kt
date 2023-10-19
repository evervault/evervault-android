package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.CreditCardType
import com.evervault.sdk.input.model.card.PaymentCard
import org.junit.Assert.assertEquals
import org.junit.Test
import com.evervault.sdk.input.model.PaymentCard as PaymentCardOld

internal class PaymentCardMapperTest {

    private val classUnderTest = PaymentCardMapper()

    @Test
    fun `Given input Then returns correctly mapped output`() {
        val input = PaymentCardOld(
            type = CreditCardType.VISA,
            number = "1122334455667788",
            bin = "11223344",
            lastFour = "7788",
            cvc = "234",
            expMonth = "12",
            expYear = "25"
        )

        val actualResult = classUnderTest.apply(input)

        val expectedResult = PaymentCard(
            type = CreditCardType.VISA,
            number = "1122334455667788",
            bin = "11223344",
            lastFour = "7788",
            cvc = "234",
            expMonth = "12",
            expYear = "25"
        )
        assertEquals(expectedResult, actualResult)
    }
}
