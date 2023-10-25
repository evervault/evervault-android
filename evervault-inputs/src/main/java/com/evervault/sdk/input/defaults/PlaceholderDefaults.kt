package com.evervault.sdk.input.defaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTexts
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTextsDefaults

@Immutable
object PlaceholderDefaults {

    @Composable
    fun texts(
        creditCardText: String = PlaceholderTextsDefaults.CreditCardText,
        expirationDateText: String = PlaceholderTextsDefaults.ExpirationDateText,
        cvcText: String = PlaceholderTextsDefaults.CvcText,
    ): PlaceholderTexts = PlaceholderTexts(
        creditCardText = creditCardText,
        expirationDateText = expirationDateText,
        cvcText = cvcText
    )
}

