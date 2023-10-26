package com.evervault.sdk.input.model.placeholder

import androidx.compose.runtime.Immutable

@Immutable
class PlaceholderTexts internal constructor(
    val creditCardText: String,
    val expirationDateText: String,
    val cvcText: String
)
