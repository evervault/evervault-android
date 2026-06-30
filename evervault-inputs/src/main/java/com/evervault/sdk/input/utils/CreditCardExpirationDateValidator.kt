package com.evervault.sdk.input.utils

import java.util.Calendar

internal object CreditCardExpirationDateValidator {

    private val allowedMonths = 1..12

    fun isValid(
        expirationMonthNumber: String,
        expirationYearLastTwoDigits: String
    ): Boolean {
        val monthNumber = expirationMonthNumber.toIntOrNull() ?: return false
        if (monthNumber !in allowedMonths) return false
        val yearInCentury = expirationYearLastTwoDigits.toIntOrNull() ?: return false

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH) + 1
        val fullYear = (currentYear / 100) * 100 + yearInCentury

        return fullYear > currentYear || (fullYear == currentYear && monthNumber >= currentMonth)
    }
}
