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
            TestData("", "", false),
            TestData("", nextYearLastTwoDigits, false),
            TestData(nextMonth, "", false),
            TestData("A", "", false),
            TestData("A", nextYearLastTwoDigits, false),
            TestData("", "A", false),
            TestData(nextMonth, "A", false),
            TestData("23", nextYearLastTwoDigits, false),
            TestData(previousMonth, previousYearLastTwoDigits, false),
            TestData(previousMonth, currentYearLastTwoDigits.toString(), false),
            TestData(previousMonth, nextYearLastTwoDigits, true),
            TestData(currentMonth, previousYearLastTwoDigits, false),
            TestData(currentMonth, currentYearLastTwoDigits.toString(), true),
            TestData(currentMonth, nextYearLastTwoDigits, true),
            TestData(nextMonth, previousYearLastTwoDigits, false),
            TestData(nextMonth, currentYearLastTwoDigits.toString(), true),
            TestData(nextMonth, nextYearLastTwoDigits, true)
        )
    }

    @Test
    fun validation() {
        val actualResult =
            CreditCardExpirationDateValidator.isValid(
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
