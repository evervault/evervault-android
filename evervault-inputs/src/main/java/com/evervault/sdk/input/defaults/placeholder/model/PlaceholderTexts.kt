package com.evervault.sdk.input.defaults.placeholder.model

import androidx.compose.runtime.Immutable

@Immutable
class PlaceholderTexts internal constructor(
    val creditCardText: String,
    val expirationDateText: String,
    val cvcText: String,
)
