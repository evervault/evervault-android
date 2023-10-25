package com.evervault.sampleapplication.ui.views.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.ui.PaymentCardInputScope

internal fun customPaymentCardInputLayout(): @Composable() (PaymentCardInputScope.(modifier: Modifier) -> Unit) =
    { CustomLayout(modifier = it) }

@Composable
private fun PaymentCardInputScope.CustomLayout(modifier: Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardImage()

        Text("CC Number", style = MaterialTheme.typography.titleMedium)
        CardNumberField(modifier = Modifier.fillMaxWidth())

        Divider()

        Text("Expiry", style = MaterialTheme.typography.titleMedium)
        ExpiryField(modifier = Modifier.fillMaxWidth())

        Divider()

        Text("CVC", style = MaterialTheme.typography.titleMedium)
        CVCField(modifier = Modifier.fillMaxWidth())
    }

}
