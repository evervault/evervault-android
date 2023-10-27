package com.evervault.sdk.input.model.placeholder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Builder that provides default parameters and configuration for the placeholders.
 */
@Immutable
object PlaceholderDefaults {

    /**
     * Provides all texts for the placeholders
     */
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

