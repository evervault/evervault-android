package com.evervault.sampleapplication.ui.views.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.evervault.sdk.input.model.placeholder.PlaceholderDefaults

@Composable
internal fun CustomTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color.Black,
            secondary = Color.White,
            primaryContainer = Color.LightGray,
        ),
        content = content
    )
}

@Composable
internal fun customPlaceholderTexts() = PlaceholderDefaults.texts(
    creditCardText = "Credit card",
    expirationDateText = "Expiry",
    cvcText = "Cvc"
)
