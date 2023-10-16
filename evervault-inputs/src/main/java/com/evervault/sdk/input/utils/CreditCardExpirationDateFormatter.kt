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
            2 -> if (input.length > 2) {
                MONTH_YEAR_PATTERN.format(monthFormatted, year)
            } else {
                monthFormatted + year
            }
            else -> ""
        }
    }

    private fun extractMonthAndYear(input: String): Pair<String, String> =
        if (input.contains("/")) {
            val monthAndYearSubstrings = input.split("/")
            var monthSubstring = monthAndYearSubstrings.getOrElse(0) { "" }
            var yearSubstring = monthAndYearSubstrings.getOrElse(1) { "" }.take(2)
            if (monthSubstring.length > 2) {
                val monthDigitCarryOver = monthSubstring.substring(2).take(1)
                yearSubstring = if (yearSubstring.length > 1) {
                    (monthDigitCarryOver + yearSubstring[1]).take(2)
                } else {
                    monthDigitCarryOver
                }
                monthSubstring = monthSubstring.dropLast(1)
            }
            monthSubstring to yearSubstring
        } else {
            val monthSubstring = input.take(2)
            val yearSubstring = if (input.length > 2) input.substring(2) else ""
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

        const val MONTH_YEAR_PATTERN = "%s/%s"
        const val YEAR_PATTERN = "/%s"
        val allowedMonthFirstDigits = 0..1
        val allowedMonthNumbers = 1..12
    }
}
