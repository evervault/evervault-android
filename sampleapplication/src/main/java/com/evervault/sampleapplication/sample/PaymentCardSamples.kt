package com.evervault.sampleapplication.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evervault.sdk.input.model.label.LabelTextsDefaults
import com.evervault.sdk.input.model.placeholder.PlaceholderTextsDefaults
import com.evervault.sdk.input.ui.PaymentCardInputScope

/**
 * This file is a partial copy of the PaymentCardWithCustomizedLayoutSample and private methods
 * from com.evervault.sdk.input.ui.sample.PaymentCardSamples
 *
 * In the future we should create a shared module so some of the components used for previews can be
 * shared without exposing them to the clients with public visibilities
 */

@Composable
internal fun PaymentCardInputScope.PaymentCardCustomLayoutWithNewComponents(modifier: Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardImage()

        CardNumberField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = LabelTextsDefaults.CreditCardText,
                    color = Color.Blue
                )
            },
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.CreditCardText,
                    color = Color(0x75757575),
                    fontSize = 12.sp
                )
            },
            textStyle = MaterialTheme.typography.titleLarge,
            textFieldColors = customTextFieldColors()
        )

        ExpiryField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = LabelTextsDefaults.ExpirationDateText,
                    color = Color.Blue
                )
            },
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.ExpirationDateText,
                    color = Color(0x9E9E9E9E),
                    fontSize = 10.sp
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,
            textFieldColors = customTextFieldColors()
        )

        CVCField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = LabelTextsDefaults.CvcText,
                    color = Color.Blue
                )
            },
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.CvcText,
                    color = Color(0xBDBDBDBD),
                    fontSize = 8.sp
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            textFieldColors = customTextFieldColors()
        )
    }
}

@Composable
internal fun PaymentCardInputScope.PaymentCardCustomLayoutWithNewComponentsWithoutLabels(modifier: Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardImage()

        CardNumberField(
            modifier = Modifier.fillMaxWidth(),
            label = null,
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.CreditCardText,
                    color = Color(0x75757575),
                    fontSize = 12.sp
                )
            },
            textStyle = MaterialTheme.typography.titleLarge,
            textFieldColors = customTextFieldColors()
        )

        ExpiryField(
            modifier = Modifier.fillMaxWidth(),
            label = null,
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.ExpirationDateText,
                    color = Color(0x9E9E9E9E),
                    fontSize = 10.sp
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,
            textFieldColors = customTextFieldColors()
        )

        CVCField(
            modifier = Modifier.fillMaxWidth(),
            label = null,
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.CvcText,
                    color = Color(0xBDBDBDBD),
                    fontSize = 8.sp
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            textFieldColors = customTextFieldColors()
        )
    }
}

@Composable
internal fun PaymentCardInputScope.PaymentCardCustomCardOnly(modifier: Modifier) {
    Column(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardImage()

        CardNumberField(
            modifier = Modifier.fillMaxWidth(),
            label = null,
            placeholder = {
                Text(
                    text = PlaceholderTextsDefaults.CreditCardText,
                    color = Color(0x75757575),
                    fontSize = 12.sp
                )
            },
            textStyle = MaterialTheme.typography.titleLarge,
            textFieldColors = customTextFieldColors()
        )
    }
}

@Composable
private fun getDefaultColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
)

@Composable
private fun customTextFieldColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
)
