package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.PaymentCardData as PaymentCardDataOld

/**
 * Temporary mapper to map [PaymentCardDataOld] to the new [PaymentCardData]
 * It will be removed when we remove the old constructor for [PaymentCardInput]
 *
 * Note: This is not proper and we should use DI for injecting the inner mappers but since it is
 * currently used in a Composable. This will be refactored after we change the [PaymentCardInput]
 * to return already mapped data and all the data logic has been removed from it.
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
