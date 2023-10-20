package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.PaymentCardData as PaymentCardDataOld

/**
 * Temporary mapper to map [PaymentCardDataOld] to the new [PaymentCardData]
 * It will be removed when we remove the old constructor for [PaymentCardInput], change its
 * internal implementation to return already mapped data, and all the data logic has been extracted from it.
 *
 * Note: This is not proper and we should use DI for injecting the inner mappers, but since it is
 * currently used in a Composable and implementing the DI would pollute the current code (already bloated)
 * it has been decided not to do it now.
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
