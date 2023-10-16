package com.evervault.sdk.input.utils

import com.evervault.sdk.input.model.CreditCardType

internal object CreditCardFormatter {

    fun formatCardNumber(cardNumber: String): String {
        val formattedCardNumber = cardNumber.filter { it.isDigit() }

        var formattedString = ""

        val groupLengths =
            if (CreditCardValidator(cardNumber).predictedType == CreditCardType.AMEX) {
                listOf(4, 6, 5)
            } else {
                listOf(4, 4, 4, 4, 3)
            }

        var currentIndex = 0
        for (length in groupLengths) {
            if (currentIndex >= formattedCardNumber.count()) {
                break
            }
            val group = formattedCardNumber.substring(
                startIndex = currentIndex,
                endIndex = minOf(currentIndex + length, formattedCardNumber.count())
            )
            formattedString += "$group "
            currentIndex += length
        }

        formattedString = formattedString.dropLast(1) // Remove trailing space

        return formattedString
    }
}
