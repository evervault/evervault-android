package com.evervault.sdk.input.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.time.YearMonth

@RunWith(Parameterized::class)
internal class CreditCardExpirationDateValidatorTest(
    private val expiryMonth: String,
    private val expiryLastTwoDigitsOfYear: String,
    private val expectedResult: Boolean,
) {

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
            arrayOf("", "", false),
            arrayOf("", nextYearLastTwoDigits, false),
            arrayOf(nextMonth, "", false),
            arrayOf("A", "", false),
            arrayOf("A", nextYearLastTwoDigits, false),
            arrayOf("", "A", false),
            arrayOf(nextMonth, "A", false),
            arrayOf("23", nextYearLastTwoDigits, false),
            arrayOf(previousMonth, previousYearLastTwoDigits, false),
            arrayOf(previousMonth, currentYearLastTwoDigits.toString(), false),
            arrayOf(previousMonth, nextYearLastTwoDigits, true),
            arrayOf(currentMonth, previousYearLastTwoDigits, false),
            arrayOf(currentMonth, currentYearLastTwoDigits.toString(), true),
            arrayOf(currentMonth, nextYearLastTwoDigits, true),
            arrayOf(nextMonth, previousYearLastTwoDigits, false),
            arrayOf(nextMonth, currentYearLastTwoDigits.toString(), true),
            arrayOf(nextMonth, nextYearLastTwoDigits, true)
        )
    }

    @Test
    fun validation() {
        val actualResult =
            CreditCardExpirationDateValidator.isValid(expiryMonth, expiryLastTwoDigitsOfYear)
        assertEquals(expectedResult, actualResult)
    }
}
