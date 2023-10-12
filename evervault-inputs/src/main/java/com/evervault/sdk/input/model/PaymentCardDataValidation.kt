package com.evervault.sdk.input.model

import com.evervault.sdk.input.utils.CreditCardExpirationDateValidator
import com.evervault.sdk.input.utils.CreditCardFormatter
import com.evervault.sdk.input.utils.CreditCardValidator
import com.evervault.sdk.input.utils.validNumberLength

internal val PaymentCardData.expiry: String get() = "${card.expMonth}/${card.expYear}"

fun createPaymentCardData(number: String, cvc: String, expiry: String): PaymentCardData {
    val validator = CreditCardValidator(number)
    val cardType = validator.predictedType
    var number = validator.string
    cardType?.let {
        number = number.take(it.validNumberLength.last())
    }

    val formattedNumber = CreditCardFormatter.formatCardNumber(number)

    val paymentCard = PaymentCard(
        type = cardType,
        number = formattedNumber,
        cvc = cvc.filter { it.isDigit() }.take(CreditCardValidator.maxCvcLength(cardType))
    )

    if (validator.isValid) {
        paymentCard.lastFour = number.takeLast(4)
        paymentCard.bin = if (cardType == CreditCardType.AMEX) number.take(6) else number.take(8)
    }

    val expiryParts = expiry.split("/")
    paymentCard.expMonth = expiryParts.getOrNull(0)?.filter { it.isDigit() } ?: ""
    paymentCard.expYear = expiryParts.getOrNull(1)?.filter { it.isDigit() } ?: ""

    val actualType = validator.actualType
    val isExpiryDateValid =
        CreditCardExpirationDateValidator.isValid(paymentCard.expMonth, paymentCard.expYear)
    val isValid = actualType != null && validator.isValid
            && CreditCardValidator.isValidCvc(cvc, actualType) && isExpiryDateValid
    val isPotentiallyValid = validator.isPotentiallyValid

    val error = when {
        !isPotentiallyValid -> PaymentCardError.InvalidPan
        !isExpiryDateValid -> PaymentCardError.InvalidMonth
        else -> null
    }

    return PaymentCardData(
        card = paymentCard,
        isValid = isValid,
        isPotentiallyValid = isPotentiallyValid,
        isEmpty = number.isEmpty(),
        error = error
    )
}

fun PaymentCardData.updateNumber(number: String): PaymentCardData {
    return createPaymentCardData(number, this.card.cvc, this.expiry)
}

fun PaymentCardData.updateCvc(cvc: String): PaymentCardData {
    return createPaymentCardData(this.card.number, cvc, this.expiry)
}

fun PaymentCardData.updateExpiry(expiry: String): PaymentCardData {
    return createPaymentCardData(this.card.number, this.card.cvc, expiry)
}

val PaymentCardError.description: String
    get() = when (this) {
        is PaymentCardError.InvalidPan -> "The credit card number you entered was invalid"
        is PaymentCardError.InvalidMonth -> "The expiration date you entered was invalid" // The object will be renamed in the future as it is a breaking change
        is PaymentCardError.EncryptionFailed -> "Encryption failed: $message"
    }
