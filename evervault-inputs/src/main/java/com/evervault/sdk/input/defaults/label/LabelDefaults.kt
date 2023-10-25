package com.evervault.sdk.input.defaults.label

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.evervault.sdk.input.defaults.label.model.LabelTexts
import com.evervault.sdk.input.defaults.label.model.LabelTextsDefaults

@Immutable
object LabelDefaults {

    @Composable
    fun texts(
        creditCardText: String = LabelTextsDefaults.CreditCardText,
        expirationDateText: String = LabelTextsDefaults.ExpirationDateText,
        cvcText: String = LabelTextsDefaults.CvcText,
    ): LabelTexts = LabelTexts(
        creditCardText = creditCardText,
        expirationDateText = expirationDateText,
        cvcText = cvcText
    )
}

