package com.evervault.sdk.input.model.card

import androidx.compose.runtime.Immutable

/**
 * Represents different kinds of errors that can occur while validating a [PaymentCard].
 */
@Immutable
sealed interface PaymentCardError {

    object InvalidPan : PaymentCardError

    object InvalidExpirationDate : PaymentCardError

    data class EncryptionFailed(val message: String?) : PaymentCardError
}
