package com.evervault.sdk.input.utils

import com.evervault.sdk.input.model.CreditCardType
import java.util.regex.Pattern

internal class CreditCardValidator(private val rawString: String) {

    val string: String get() = rawString.onlyDigits()

    companion object {
        private val types = CreditCardType.values().toList()

        private const val maxCvcLength = 3
        private const val maxCvcLengthAmex = 4

        fun maxCvcLength(type: CreditCardType?): Int {
            return type?.let {
                when (it) {
                    CreditCardType.AMEX -> maxCvcLengthAmex
                    else -> maxCvcLength
                }
            } ?: types.map { maxCvcLength(it) }.maxOrNull()!!
        }

        fun isValidCvc(cvc: String, type: CreditCardType): Boolean {
            return cvc.trim().length == maxCvcLength(type)
        }

        private fun isValid(string: String): Boolean {
            val digits = string.reversed().map { it.toString().toInt() }
            val result = digits.withIndex().fold(Calculation(0, 0)) { acc, indexedValue ->
                val odd = if (indexedValue.index % 2 != 0) acc.odd + (indexedValue.value / 5 + (2 * indexedValue.value) % 10) else acc.odd
                val even = if (indexedValue.index % 2 == 0) acc.even + indexedValue.value else acc.even
                Calculation(odd, even)
            }
            return result.result()
        }

        private fun matchesRegex(string: String, regex: String): Boolean {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(string)
            return matcher.matches()
        }

        private const val invalidRegex = "^(?:1|7|9).*"
    }

    val predictedType: CreditCardType?
        get() = types.firstOrNull { type ->
            matchesRegex(string, "^${type.prefixRegex}.*$")
        }

    val actualType: CreditCardType?
        get() = types.firstOrNull { type ->
            matchesRegex(string, type.regex)
        }

    val isValid: Boolean
        get() {
            val type = actualType ?: return false
            val isValidLength = type.validNumberLength.contains(string.length)
            return isValidLength && Companion.isValid(string)
        }

    val isPotentiallyValid: Boolean
        get() {
            if (isValid) return true
            val type = predictedType
            return if (type == null) {
                !matchesRegex(string, invalidRegex) && string.length <= 6
            } else {
                string.length < type.validNumberLength.last()
            }
        }

    fun isValid(type: CreditCardType): Boolean {
        return isValid && actualType == type
    }
}

data class Calculation(val odd: Int, val even: Int) {
    fun result() = (odd + even) % 10 == 0
}

val CreditCardType.prefixRegex: String get() = when(this) {
    CreditCardType.AMEX -> "3[47]"
    CreditCardType.DINERS_CLUB -> "3(?:0[0-5]|[68][0-9])"
    CreditCardType.DISCOVER -> "6(?:011|5[0-9]{2})"
    CreditCardType.JCB -> "(?:2131|1800|35[0-9]{3})"
    CreditCardType.ELO -> "(?:401178|401179|438935|457631|457632|431274|451416|457393|504175|506699|506778|509000|509999|627780|636297|636368|650031|650033|650035|650051|650405|650439|650485|650538|650541|650598|650700|650718|650720|650727|650901|650978|651652|651679|655000|655019|655021|655058)"
    CreditCardType.HIPER -> "(?:637095|63737423|63743358|637568|637599|637609|637612)"
    CreditCardType.HIPERCARD -> "606282"
    CreditCardType.MAESTRO -> "(?:5[0678]\\d\\d|6304|6390|67\\d\\d)"
    CreditCardType.MASTERCARD -> "(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)"
    CreditCardType.MIR -> "220[0-4]"
    CreditCardType.UNION_PAY -> "62[0-5]"
    CreditCardType.VISA -> "4\\d{5}"
}

val CreditCardType.remainingRegex: String get() = when(this) {
    CreditCardType.AMEX -> "[0-9]{5,}"
    CreditCardType.DINERS_CLUB -> "[0-9]{4,}"
    CreditCardType.DISCOVER -> "[0-9]{3,}"
    CreditCardType.JCB -> "[0-9]{3,}"
    CreditCardType.ELO -> "\\d{10}"
    CreditCardType.HIPER -> "\\d{8,10}"
    CreditCardType.HIPERCARD -> "\\d{8}"
    CreditCardType.MAESTRO -> "\\d{8,15}"
    CreditCardType.MASTERCARD -> "[0-9]{12}"
    CreditCardType.MIR -> "\\d{12,15}"
    CreditCardType.UNION_PAY -> "\\d{13,16}"
    CreditCardType.VISA -> "\\d{7,10}"
}

val CreditCardType.regex: String get() = "^$prefixRegex$remainingRegex$"

val CreditCardType.validNumberLength: Set<Int> get() = when(this) {
    CreditCardType.VISA -> setOf(13, 16)
    CreditCardType.AMEX -> setOf(15)
    CreditCardType.MAESTRO -> IntRange(12, 19).toSet()
    CreditCardType.DINERS_CLUB -> IntRange(14, 19).toSet()
    CreditCardType.JCB, CreditCardType.DISCOVER, CreditCardType.UNION_PAY, CreditCardType.MIR -> IntRange(16, 19).toSet()
    else -> setOf(16)
}

internal fun String.onlyDigits(): String = filter { it.isDigit() }
