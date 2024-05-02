package com.evervault.sampleapplication.navigation

sealed class Route(val route: String) {

    object Home : Route("home")

    object BasicEncryption : Route("BasicEncryption")

    object FileEncryption : Route("FileEncryption")

    object CreditCardInput : Route("CreditCardInput")

    object CreditCardInputWithPlaceholders : Route("CreditCardInputWithPlaceholders")

    object CreditCardInputCustom : Route("CreditCardInputCustom")

    object CreditCardInputRows : Route("CreditCardInputRows")

    object CreditCardInputRowsWithPlaceholders : Route("CreditCardInputRowsWithPlaceholders")

    object CreditCardInputCustomStyle : Route("CreditCardInputCustomStyle")

    object InlinePaymentCard : Route("InlinePaymentCard")

    object InlinePaymentCardCustomStyle : Route("InlinePaymentCardCustomStyle")

    object RowsPaymentCard : Route("RowsPaymentCard")

    object CreditCardInputCustomComposables : Route("CreditCardInputCustomComposables")

    object CreditCardInputCustomComposablesWithoutLabels :
        Route("CreditCardInputCustomComposablesWithoutLabels")

    object CreditCardInputCustomCardOnly: Route("CreditCardInputCustomCardOnly")

    object Enclave : Route("Enclave")
}
