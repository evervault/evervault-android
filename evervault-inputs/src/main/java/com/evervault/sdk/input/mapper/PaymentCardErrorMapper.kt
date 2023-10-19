package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCardError
import com.evervault.sdk.input.model.PaymentCardError as PaymentCardErrorOld
import com.evervault.sdk.input.model.PaymentCardError.EncryptionFailed as EncryptionFailedOld
import com.evervault.sdk.input.model.PaymentCardError.InvalidMonth as InvalidMonthOld
import com.evervault.sdk.input.model.PaymentCardError.InvalidPan as InvalidPanOld

/**
 * Temporary mapper to map [PaymentCardErrorOld] to the new [PaymentCardError]
 * It will be removed when we remove the old constructor for [PaymentCardInput]
 */
internal class PaymentCardErrorMapper {

    fun apply(input: PaymentCardErrorOld?) = when (input) {
        is EncryptionFailedOld -> PaymentCardError.EncryptionFailed(input.message)
        InvalidMonthOld -> PaymentCardError.InvalidExpirationDate
        InvalidPanOld -> PaymentCardError.InvalidPan
        null -> null
    }
}
