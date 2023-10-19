package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.PaymentCardData as PaymentCardDataOld

/**
 * Temporary mapper to map [PaymentCardDataOld] to the new [PaymentCardData]
 * It will be removed when we remove the old constructor for [PaymentCardInput]
 */
internal class PaymentCardDataMapper {

    private val paymentCardMapper = PaymentCardMapper()
    private val errorMapper = PaymentCardErrorMapper()

    fun apply(input: PaymentCardDataOld) = PaymentCardData(
        card = paymentCardMapper.apply(input.card),
        isValid = input.isValid,
        isPotentiallyValid = input.isPotentiallyValid,
        isEmpty = input.isEmpty,
        error = errorMapper.apply(input.error)
    )
}
