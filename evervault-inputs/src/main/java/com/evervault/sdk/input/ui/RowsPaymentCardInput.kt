package com.evervault.sdk.input.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PaymentCardInputScope.rows(
    modifier: Modifier = Modifier,
    fieldPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardNumberField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(fieldPadding)
            )
            CardImage(
                modifier = Modifier
                    .padding(end =  10.dp)
                    .width(30.dp)
            )
        }

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExpiryField(modifier = Modifier
                .weight(1f, true)
                .padding(fieldPadding)
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            CVCField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(fieldPadding),
                options = PaymentCardInputScope.TextFieldOptions(
                    textStyle = { copy(textAlign = TextAlign.End) }
                )
            )
        }
    }
}

@Composable
fun rowsPaymentCardInputLayout(): @Composable() (PaymentCardInputScope.(Modifier) -> Unit) = { rows(modifier = it) }
