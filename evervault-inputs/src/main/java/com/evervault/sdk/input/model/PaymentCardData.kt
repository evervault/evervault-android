package com.evervault.sdk.input.model

/**
 * `PaymentCardData` represents the state of a payment card, including its validity and any associated errors.
 */
data class PaymentCardData(
    var card: PaymentCard = PaymentCard(),
    var isValid: Boolean = false,
    var isPotentiallyValid: Boolean = true,
    var isEmpty: Boolean = true,
    var error: PaymentCardError? = null,
)

/**
 * `PaymentCard` represents a credit card with its essential information like card number, cvc, expiration month and year.
 */
data class PaymentCard(
    var type: CreditCardType? = null,
    var number: String = "",
    var cvc: String = "",
    var expMonth: String = "",
    var expYear: String = "",
)

/**
 * `PaymentCardError` represents different kinds of errors that can occur while validating a `PaymentCard`.
 */
sealed class PaymentCardError {
    object InvalidPan: PaymentCardError()
    object InvalidMonth: PaymentCardError()
    data class EncryptionFailed(val message: String?): PaymentCardError()
}