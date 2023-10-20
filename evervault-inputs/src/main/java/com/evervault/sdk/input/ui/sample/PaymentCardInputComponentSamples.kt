package com.evervault.sdk.input.ui.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.ui.PaymentCardInputComponent
import com.evervault.sdk.input.ui.PaymentCardInputScope
import com.evervault.sdk.input.ui.rowsPaymentCardInputLayout

@Preview
@Composable
internal fun PaymentCardInputComponentDefaultLayoutPreview() {
    PaymentCardInputComponent()
}

@Preview
@Composable
internal fun PaymentCardInputComponentRowsLayoutPreview() {
    PaymentCardInputComponent(content = rowsPaymentCardInputLayout())
}

@Preview
@Composable
internal fun PaymentCardInputComponentCustomLayoutPreview() {
    val modifier = Modifier
        .background(color = Color.Blue, RoundedCornerShape(16.dp))
        .padding(24.dp)

    PaymentCardInputComponent(
        textStyle = TextStyle.Default.copy(color = Color.Yellow),
        placeholderTextStyle = TextStyle.Default.copy(color = Color.LightGray),
        modifier = Modifier
            .background(color = Color.Blue, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
        ) {
            Column {
                CardNumberField(
                    options = PaymentCardInputScope.TextFieldOptions { TextStyle.Default.copy(color = Color.Red) },
                    placeholder = "Custom card placeholder",
                    modifier = modifier
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    ExpiryField(
                        options = PaymentCardInputScope.TextFieldOptions {
                            TextStyle.Default.copy(color = Color.Yellow)
                        },
                        placeholder = "Custom expiry placeholder",
                        modifier = modifier
                    )
                }
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    CVCField(
                        options = PaymentCardInputScope.TextFieldOptions(),
                        placeholder = "Custom CVC placeholder",
                        modifier = modifier
                    )
                }
            }
        }
    }
}
