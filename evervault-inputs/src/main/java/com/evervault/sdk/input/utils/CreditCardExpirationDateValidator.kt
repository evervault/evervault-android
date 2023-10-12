package com.evervault.sdk.input.utils

import java.time.LocalDate
import java.time.YearMonth

internal object CreditCardExpirationDateValidator {

    private val allowedMonths = 1..12

    fun isValid(
        expirationMonthNumber: String,
        expirationYearLastTwoDigits: String
    ): Boolean {
        val monthNumber = expirationMonthNumber.toIntOrNull() ?: return false
        if (monthNumber !in allowedMonths) return false
        val yearInCentury = expirationYearLastTwoDigits.toIntOrNull() ?: return false
        return getExpirationDate(monthNumber, yearInCentury) >= LocalDate.now()
    }

    private fun getExpirationDate(monthNumber: Int, lastTwoDigitsOfYear: Int): LocalDate {
        val currentCentury = YearMonth.now().year / 100
        return YearMonth.of(currentCentury * 100 + lastTwoDigitsOfYear, monthNumber).atEndOfMonth()
    }
}
