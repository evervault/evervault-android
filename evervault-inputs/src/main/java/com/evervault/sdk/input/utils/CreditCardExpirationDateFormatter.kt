package com.evervault.sdk.input.utils

internal class CreditCardExpirationDateFormatter {

    internal fun format(input: String): String {
        val (month, year) = extractMonthAndYear(input)
        val monthFormatted = formatMonth(month)

        return when (monthFormatted.length) {
            0 -> if (year.isNotBlank()) YEAR_PATTERN.format(year) else ""
            1 -> if (year.isNotBlank()) {
                MONTH_YEAR_PATTERN.format(monthFormatted, year)
            } else {
                monthFormatted
            }
            2 -> if (input.length > MAX_DIGITS_PER_NUMBER) {
                MONTH_YEAR_PATTERN.format(monthFormatted, year)
            } else {
                monthFormatted + year
            }
            else -> ""
        }
    }

    private fun extractMonthAndYear(input: String): Pair<String, String> =
        if (input.contains(DATE_SEPARATOR)) {
            val monthAndYearSubstrings = input.split(DATE_SEPARATOR)
            var monthSubstring = monthAndYearSubstrings.getOrElse(0) { "" }
            var yearSubstring = monthAndYearSubstrings
                .getOrElse(1) { "" }
                .take(MAX_DIGITS_PER_NUMBER)
            if (monthSubstring.length > 2) {
                val monthDigitCarryOver = monthSubstring.substring(MAX_DIGITS_PER_NUMBER).take(1)
                yearSubstring = if (yearSubstring.length > 1) {
                    (monthDigitCarryOver + yearSubstring[1]).take(MAX_DIGITS_PER_NUMBER)
                } else {
                    monthDigitCarryOver
                }
                monthSubstring = monthSubstring.dropLast(1)
            }
            monthSubstring to yearSubstring
        } else {
            val monthSubstring = input.take(MAX_DIGITS_PER_NUMBER)
            val yearSubstring = if (input.length > MAX_DIGITS_PER_NUMBER) {
                input.substring(MAX_DIGITS_PER_NUMBER)
            } else {
                ""
            }
            monthSubstring to yearSubstring
        }

    private fun formatMonth(monthNumber: String): String {
        val monthNumberDigits = monthNumber.toIntOrNull() ?: return ""
        val firstMonthDigit = monthNumber.getOrNull(0)?.digitToIntOrNull()
        val secondMonthDigit = monthNumber.getOrNull(1)?.digitToIntOrNull()

        val firstMonthCharacter = if (firstMonthDigit in allowedMonthFirstDigits) {
            firstMonthDigit.toString()
        } else {
            ""
        }

        val secondMonthCharacter = if (monthNumberDigits in allowedMonthNumbers) {
            secondMonthDigit?.toString() ?: ""
        } else {
            ""
        }

        return firstMonthCharacter + secondMonthCharacter
    }

    private companion object {

        const val MAX_DIGITS_PER_NUMBER = 2
        const val DATE_SEPARATOR = "/"
        const val MONTH_YEAR_PATTERN = "%s$DATE_SEPARATOR%s"
        const val YEAR_PATTERN = "$DATE_SEPARATOR%s"

        val allowedMonthFirstDigits = 0..1
        val allowedMonthNumbers = 1..12
    }
}
