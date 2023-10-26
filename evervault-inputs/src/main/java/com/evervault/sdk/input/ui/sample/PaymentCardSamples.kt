package com.evervault.sdk.input.ui.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evervault.sdk.input.defaults.label.model.LabelTextsDefaults
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTexts
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTextsDefaults
import com.evervault.sdk.input.ui.InlinePaymentCard
import com.evervault.sdk.input.ui.PaymentCard
import com.evervault.sdk.input.ui.PaymentCardInputScope
import com.evervault.sdk.input.ui.RowsPaymentCard

@Preview
@Composable
internal fun InlinePaymentCardPreview() {
    InlinePaymentCard()
}

@Preview
@Composable
internal fun InlineCustomizedPaymentCardPreview() {
    InlinePaymentCard(
        modifier = Modifier
            .padding(12.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        textStyle = TextStyle.Default.copy(color = Color.Blue),
        placeholderTexts = customPlaceholderTexts(),
        placeholderTextStyle = TextStyle.Default.copy(color = Color.LightGray),
    )
}

@Preview
@Composable
internal fun InlinePaymentCardWithThemePreview() {
    CustomTheme {
        InlinePaymentCard()
    }
}

@Preview
@Composable
internal fun RowsPaymentCardPreview() {
    RowsPaymentCard()
}

@Preview
@Composable
internal fun CustomizedRowsPaymentCardPreview() {
    RowsPaymentCard(
        modifier = Modifier
            .padding(12.dp)
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        textStyle = TextStyle.Default.copy(color = Color.Blue),
        placeholderTexts = customPlaceholderTexts(),
        placeholderTextStyle = TextStyle.Default.copy(color = Color.LightGray),
    )
}

@Preview
@Composable
internal fun RowsPaymentCardWithThemePreview() {
    CustomTheme {
        RowsPaymentCard()
    }
}

@Preview
@Composable
internal fun PaymentCardCustomLayoutWithOldComponentsPreview() {
    PaymentCard(
        textStyle = TextStyle.Default.copy(color = Color.Yellow),
        placeholderTextStyle = TextStyle.Default.copy(color = Color.LightGray),
        modifier = Modifier
            .background(color = Color(0x8282B1FF), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) { modifier ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CardImage(Modifier.size(50.dp))

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
                ExpiryField(
                    options = PaymentCardInputScope.TextFieldOptions {
                        TextStyle.Default.copy(color = Color.Yellow)
                    },
                    placeholder = "Custom expiry placeholder",
                    modifier = modifier.weight(0.5f)
                )

                CVCField(
                    options = PaymentCardInputScope.TextFieldOptions(),
                    placeholder = "Custom CVC placeholder",
                    modifier = modifier.weight(0.5f)
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PaymentCardCustomLayoutWithNewComponentsPreview() {
    PaymentCard(
        textStyle = TextStyle.Default.copy(color = Color.Yellow),
        placeholderTextStyle = TextStyle.Default.copy(color = Color.LightGray),
        modifier = Modifier
            .background(color = Color(0x8282B1FF), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = it
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CardImage()

            CardNumberField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = LabelTextsDefaults.CreditCardText, color = Color.Red) },
                placeholder = {
                    Text(
                        text = PlaceholderTextsDefaults.CreditCardText,
                        color = Color.Gray
                    )
                },
                textStyle = MaterialTheme.typography.bodySmall,
                textFieldColors = TextFieldDefaults.colors()
            )

            ExpiryField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = LabelTextsDefaults.ExpirationDateText,
                        color = Color.Yellow
                    )
                },
                placeholder = {
                    Text(
                        text = PlaceholderTextsDefaults.ExpirationDateText,
                        color = Color.Gray
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                textFieldColors = customTextFieldColors()
            )

            CVCField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = LabelTextsDefaults.CvcText, color = Color.Blue) },
                placeholder = {
                    Text(
                        text = PlaceholderTextsDefaults.CvcText,
                        color = Color.LightGray,
                        fontSize = 8.sp
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium,
                textFieldColors = customTextFieldColors()
            )
        }
    }
}

@Composable
private fun CustomTheme(content: @Composable () -> Unit) {
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
private fun customPlaceholderTexts() = PlaceholderTexts(
    creditCardText = "Custom credit card text",
    expirationDateText = "Custom expiry date text",
    cvcText = "Custom CVC text"
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
