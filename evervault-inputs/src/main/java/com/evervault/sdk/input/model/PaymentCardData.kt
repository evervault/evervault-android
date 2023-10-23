package com.evervault.sdk.input.model

/**
 * `PaymentCardData` represents the state of a payment card, including its validity and any associated errors.
 */
@Deprecated(
    message = "Use the other PaymentCardData in com.evervault.sdk.input.model.card",
    replaceWith = ReplaceWith(
        expression = "PaymentCardData",
        imports = ["com.evervault.sdk.input.model.card.PaymentCardData"]
    )
)
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
@Deprecated(
    message = "Use the other PaymentCard in com.evervault.sdk.input.model.card",
    replaceWith = ReplaceWith(
        expression = "PaymentCard",
        imports = ["com.evervault.sdk.input.model.card.PaymentCard"]
    )
)
data class PaymentCard(
    var type: CreditCardType? = null,
    var number: String = "",
    var bin: String = "",
    var lastFour: String = "",
    var cvc: String = "",
    var expMonth: String = "",
    var expYear: String = "",
)

/**
 * `PaymentCardError` represents different kinds of errors that can occur while validating a `PaymentCard`.
 */
@Deprecated(
    message = "Use the other PaymentCardError in com.evervault.sdk.input.model.card",
    replaceWith = ReplaceWith(
        expression = "PaymentCardError",
        imports = ["com.evervault.sdk.input.model.card.PaymentCardError"]
    )
)
sealed class PaymentCardError {
    object InvalidPan : PaymentCardError()
    object InvalidMonth : PaymentCardError()
    data class EncryptionFailed(val message: String?) : PaymentCardError()
}
