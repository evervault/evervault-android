package com.evervault.sdk.input.utils

import com.evervault.sdk.input.model.CreditCardType

internal object CreditCardFormatter {

    fun formatCardNumber(cardNumber: String): String {
        val formattedCardNumber = cardNumber.filter { it.isDigit() }

        var formattedString = ""

        val groupLengths = if (CreditCardValidator(cardNumber).predictedType == CreditCardType.AMEX) {
            listOf(4, 6, 5)
        } else {
            listOf(4, 4, 4, 4, 3)
        }

        var currentIndex = 0
        for (length in groupLengths) {
            if (currentIndex >= formattedCardNumber.count()) {
                break
            }
            val group = formattedCardNumber.substring(currentIndex, minOf(currentIndex + length, formattedCardNumber.count()))
            formattedString += "$group "
            currentIndex += length
        }

        formattedString = formattedString.dropLast(1) // Remove trailing space

        return formattedString
    }

    fun formatExpiryDate(value: String): String {
        var formatted = value.take(5)
        when (formatted.length) {
            1, 2 -> {
                formatted = formatted.filter { it.isDigit() }
                val month = formatted.toIntOrNull() ?: return ""
                if (formatted.length == 2) {
                    formatted = "$formatted/"
                } else if (month > 1) {
                    formatted = "0$formatted/"
                }
            }
        }
        return formatted
    }
}
