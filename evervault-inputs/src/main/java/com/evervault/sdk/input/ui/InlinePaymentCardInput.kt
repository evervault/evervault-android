package com.evervault.sdk.input.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentCardInputScope.inline(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(16.dp)
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(padding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CardImage(modifier = Modifier.width(30.dp))
        CardNumberField(modifier = Modifier.weight(0.66f, true))
        ExpiryField(modifier = Modifier.weight(0.20f, true))
        CVCField(modifier = Modifier.weight(0.14f, true))
    }
}

@Composable
fun inlinePaymentCardInputLayout(): @Composable() (PaymentCardInputScope.(Modifier) -> Unit) = { inline(modifier = it) }
