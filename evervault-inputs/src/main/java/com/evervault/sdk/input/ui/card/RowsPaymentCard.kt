package com.evervault.sdk.input.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.placeholder.PlaceholderDefaults
import com.evervault.sdk.input.model.placeholder.PlaceholderTexts

/**
 * Represents the card input form with with predefined rows layout.
 *
 * [Default rows card image](https://raw.githubusercontent.com/evervault/evervault-android/main/rows.png)
 *
 * For simplicity of use, the layout is provided as is, and cannot be changed (besides standard modifiers,
 * text styles and placeholder texts).
 * To manually customize the layout appearance use the [PaymentCard] component
 *
 * Samples:
 * @sample com.evervault.sdk.input.ui.sample.RowsPaymentCardSample
 * @sample com.evervault.sdk.input.ui.sample.RowsCustomizedPaymentCardSample
 * @sample com.evervault.sdk.input.ui.sample.RowsPaymentCardWithAppliedThemeSample
 *
 * A common use case is to use it when no specific changes have to need to be applied to the layout,
 * but only basic customization like text and placeholder styles, placeholder texts and layout modifiers.
 * The layout is provided in two rows: credit card is in the first, and expiration date and CVC in the second row
 * For more information, see [Styling docs](https://docs.evervault.com/sdks/android#styling)
 *
 * @param modifier the [Modifier] to be applied to the layout
 * @param textStyle the [TextStyle] to be applied to the input texts
 * @param placeholderTexts the [PlaceholderTexts] to be applied to the text input placeholders
 * @param placeholderTextStyle the [TextStyle] to be applied to the text input placeholders
 * @param onDataChange the listener to be invoked when the underlying card data changes
 */
@Composable
fun RowsPaymentCard(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTexts: PlaceholderTexts = PlaceholderDefaults.texts(),
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    onDataChange: (PaymentCardData) -> Unit = {}
) {
    PaymentCard(
        modifier = modifier,
        textStyle = textStyle,
        placeholderTextStyle = placeholderTextStyle,
        onDataChange = onDataChange
    ) {
        RowsLayout(
            modifier = it,
            placeholderTexts = placeholderTexts,
        )
    }
}

@Composable
private fun PaymentCardInputScope.RowsLayout(
    modifier: Modifier,
    placeholderTexts: PlaceholderTexts,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    .padding(padding),
                options = PaymentCardInputScope.TextFieldOptions(),
                placeholder = placeholderTexts.creditCardText
            )

            CardImage(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .width(30.dp)
            )
        }

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExpiryField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(padding),
                options = PaymentCardInputScope.TextFieldOptions(),
                placeholder = placeholderTexts.expirationDateText
            )

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            CVCField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(padding),
                options = PaymentCardInputScope.TextFieldOptions(
                    textStyle = { copy(textAlign = TextAlign.End) }
                ),
                placeholder = placeholderTexts.cvcText
            )
        }
    }
}
