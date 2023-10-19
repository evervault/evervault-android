package com.evervault.sdk.input.model.card

val PaymentCardError.description: String
    get() = when (this) {
        PaymentCardError.InvalidPan -> "The credit card number you entered was invalid"
        PaymentCardError.InvalidExpirationDate -> "The expiration date you entered was invalid"
        is PaymentCardError.EncryptionFailed -> "Encryption failed: $message"
    }
