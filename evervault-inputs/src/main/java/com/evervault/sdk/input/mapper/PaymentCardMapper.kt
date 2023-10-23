package com.evervault.sdk.input.mapper

import com.evervault.sdk.input.model.card.PaymentCard
import com.evervault.sdk.input.model.PaymentCard as PaymentCardOld

/**
 * Temporary mapper to map [PaymentCardOld] to the new [PaymentCard]
 * It will be removed when we remove the old constructor for [PaymentCardInput]
 */
internal class PaymentCardMapper {

    fun apply(input: PaymentCardOld) = PaymentCard(
        type = input.type,
        number = input.number,
        bin = input.bin,
        lastFour = input.lastFour,
        cvc = input.cvc,
        expMonth = input.expMonth,
        expYear = input.expYear
    )
}
