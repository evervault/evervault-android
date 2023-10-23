package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCardError
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import com.evervault.sdk.input.model.PaymentCardError as PaymentCardErrorOld
import com.evervault.sdk.input.model.PaymentCardError.EncryptionFailed as EncryptionFailedOld
import com.evervault.sdk.input.model.PaymentCardError.InvalidMonth as InvalidMonthOld
import com.evervault.sdk.input.model.PaymentCardError.InvalidPan as InvalidPanOld

@RunWith(Parameterized::class)
internal class PaymentCardErrorMapperTest(private val testData: TestData) {

    companion object {

        @JvmStatic
        @Parameters(name = "testData = {0}")
        fun data() = listOf(
            TestData(
                input = null,
                expectedResult = null
            ),
            TestData(
                input = EncryptionFailedOld("message"),
                expectedResult = PaymentCardError.EncryptionFailed("message")
            ),
            TestData(
                input = InvalidMonthOld,
                expectedResult = PaymentCardError.InvalidExpirationDate
            ),
            TestData(
                input = InvalidPanOld,
                expectedResult = PaymentCardError.InvalidPan
            )
        )
    }

    private val classUnderTest = PaymentCardErrorMapper()

    @Test
    fun validation() {
        val actualResult = classUnderTest.apply(testData.input)

        assertEquals(testData.expectedResult, actualResult)
    }

    internal data class TestData(
        val input: PaymentCardErrorOld?,
        val expectedResult: PaymentCardError?
    )
}
