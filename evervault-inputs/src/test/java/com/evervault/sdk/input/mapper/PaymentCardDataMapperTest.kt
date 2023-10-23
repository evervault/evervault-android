package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.CreditCardType
import com.evervault.sdk.input.model.card.PaymentCard
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.card.PaymentCardError
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import com.evervault.sdk.input.model.PaymentCard as PaymentCardOld
import com.evervault.sdk.input.model.PaymentCardData as PaymentCardDataOld
import com.evervault.sdk.input.model.PaymentCardError as PaymentCardErrorOld

@RunWith(Parameterized::class)
internal class PaymentCardDataMapperTest(private val testData: TestData) {

    private val classUnderTest = PaymentCardDataMapper()

    companion object {

        @JvmStatic
        @Parameters(name = "testData = {0}")
        fun data() = listOf(
            TestData(
                input = PaymentCardDataOld(
                    card = paymentCardOld,
                    isValid = false,
                    isPotentiallyValid = false,
                    isEmpty = false,
                    error = null
                ),
                expectedResult = PaymentCardData(
                    card = paymentCard,
                    isValid = false,
                    isPotentiallyValid = false,
                    isEmpty = false,
                    error = null
                )
            ),
            TestData(
                input = PaymentCardDataOld(
                    card = paymentCardOld,
                    isValid = true,
                    isPotentiallyValid = true,
                    isEmpty = true,
                    error = PaymentCardErrorOld.InvalidPan
                ),
                expectedResult = PaymentCardData(
                    card = paymentCard,
                    isValid = true,
                    isPotentiallyValid = true,
                    isEmpty = true,
                    error = PaymentCardError.InvalidPan
                )
            ),
            TestData(
                input = PaymentCardDataOld(
                    card = paymentCardOld,
                    isValid = false,
                    isPotentiallyValid = true,
                    isEmpty = false,
                    error = PaymentCardErrorOld.InvalidMonth
                ),
                expectedResult = PaymentCardData(
                    card = paymentCard,
                    isValid = false,
                    isPotentiallyValid = true,
                    isEmpty = false,
                    error = PaymentCardError.InvalidExpirationDate
                )
            )
        )

        private val paymentCardOld = PaymentCardOld(
            type = CreditCardType.VISA,
            number = "1122334455667788",
            bin = "11223344",
            lastFour = "7788",
            cvc = "234",
            expMonth = "12",
            expYear = "25"
        )

        private val paymentCard = PaymentCard(
            type = CreditCardType.VISA,
            number = "1122334455667788",
            bin = "11223344",
            lastFour = "7788",
            cvc = "234",
            expMonth = "12",
            expYear = "25"
        )
    }

    @Test
    fun validation() {
        val actualResult = classUnderTest.apply(testData.input)

        assertEquals(testData.expectedResult, actualResult)
    }

    data class TestData(
        val input: PaymentCardDataOld,
        val expectedResult: PaymentCardData
    )
}
