package com.evervault.sdk.input.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.time.YearMonth

@RunWith(Parameterized::class)
internal class CreditCardExpirationDateValidatorTest(private val testData: TestData) {

    companion object {

        private val currentYearMonth = YearMonth.now()
        private val currentYearLastTwoDigits = currentYearMonth.year % 100
        private val previousYearLastTwoDigits = (currentYearLastTwoDigits - 1).toString()
        private val nextYearLastTwoDigits = (currentYearLastTwoDigits + 1).toString()
        private val previousMonth = (currentYearMonth.minusMonths(1)).monthValue.toString()
        private val currentMonth = currentYearMonth.monthValue.toString()
        private val nextMonth = (currentYearMonth.plusMonths(1)).monthValue.toString()

        @JvmStatic
        @Parameters(name = "Given expiryMonth = {0} and expiryLastTwoDigitsOfYear = {1} Then returns expectedResult = {2}")
        fun data() = listOf(
            TestData(expiryMonth = "", expiryLastTwoDigitsOfYear = "", expectedResult = false),
            TestData(
                expiryMonth = "",
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(
                expiryMonth = nextMonth,
                expiryLastTwoDigitsOfYear = "",
                expectedResult = false
            ),
            TestData(expiryMonth = "A", expiryLastTwoDigitsOfYear = "", expectedResult = false),
            TestData(
                expiryMonth = "A",
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(expiryMonth = "", expiryLastTwoDigitsOfYear = "A", expectedResult = false),
            TestData(
                expiryMonth = nextMonth,
                expiryLastTwoDigitsOfYear = "A",
                expectedResult = false
            ),
            TestData(
                expiryMonth = "23",
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(
                expiryMonth = previousMonth,
                expiryLastTwoDigitsOfYear = previousYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(
                expiryMonth = previousMonth,
                expiryLastTwoDigitsOfYear = currentYearLastTwoDigits.toString(),
                expectedResult = false
            ),
            TestData(
                expiryMonth = previousMonth,
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = true
            ),
            TestData(
                expiryMonth = currentMonth,
                expiryLastTwoDigitsOfYear = previousYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(
                expiryMonth = currentMonth,
                expiryLastTwoDigitsOfYear = currentYearLastTwoDigits.toString(),
                expectedResult = true
            ),
            TestData(
                expiryMonth = currentMonth,
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = true
            ),
            TestData(
                expiryMonth = nextMonth,
                expiryLastTwoDigitsOfYear = previousYearLastTwoDigits,
                expectedResult = false
            ),
            TestData(
                expiryMonth = nextMonth,
                expiryLastTwoDigitsOfYear = currentYearLastTwoDigits.toString(),
                expectedResult = true
            ),
            TestData(
                expiryMonth = nextMonth,
                expiryLastTwoDigitsOfYear = nextYearLastTwoDigits,
                expectedResult = true
            )
        )
    }

    @Test
    fun validation() {
        val actualResult = CreditCardExpirationDateValidator.isValid(
            expirationMonthNumber = testData.expiryMonth,
            expirationYearLastTwoDigits = testData.expiryLastTwoDigitsOfYear
        )
        assertEquals(testData.expectedResult, actualResult)
    }

    internal data class TestData(
        val expiryMonth: String,
        val expiryLastTwoDigitsOfYear: String,
        val expectedResult: Boolean,
    )
}
