package com.evervault.sdk.input.model.card

import androidx.compose.runtime.Immutable
import com.evervault.sdk.input.model.CreditCardType

/**
 * `PaymentCard` represents a credit card with its essential information like card number, cvc, expiration month and year.
 */
@Immutable
data class PaymentCard(
    val type: CreditCardType? = null,
    val number: String = "",
    val bin: String = "",
    val lastFour: String = "",
    val cvc: String = "",
    val expMonth: String = "",
    val expYear: String = "",
)
