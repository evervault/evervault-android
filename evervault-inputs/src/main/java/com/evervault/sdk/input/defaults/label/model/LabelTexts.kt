package com.evervault.sdk.input.defaults.label.model

import androidx.compose.runtime.Immutable

@Immutable
class LabelTexts internal constructor(
    val creditCardText: String,
    val expirationDateText: String,
    val cvcText: String,
)
