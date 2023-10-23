package com.evervault.sdk.input.model.card

import androidx.compose.runtime.Immutable

/**
 * `PaymentCardData` represents the state of a payment card, including its validity and any associated errors.
 */
@Immutable
data class PaymentCardData(
    val card: PaymentCard = PaymentCard(),
    val isValid: Boolean = false,
    val isPotentiallyValid: Boolean = true,
    val isEmpty: Boolean = true,
    val error: PaymentCardError? = null,
)
