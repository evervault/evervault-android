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
import com.evervault.sdk.input.defaults.placeholder.PlaceholderDefaults
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTexts
import com.evervault.sdk.input.model.card.PaymentCardData

/**
 * Represents the whole card input form with default styles and inline layout.
 *
 * [Default inlined content image](https://github.com/evervault/evervault-android/blob/main/inline.png?raw=true)
 *
 * Sample:
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardDefaultLayoutPreview
 *
 * A common use case is to use it with default or custom parameters to customize specific appearance.
 * The layout is inlined in one row.
 * For more information, see [Inputs docs](https://docs.evervault.com/sdks/android#inputs)
 *
 * @param modifier the [Modifier] to be applied to the layout
 * @param textStyle the [TextStyle] to be applied to the input texts
 * @param placeholderTextStyle the [TextStyle] to be applied to the input texts placeholders
 * @param onDataChange the listener to be invoked when the underlying card data changes
 * @param content a lambda to provide the user content layout
 */
@Composable
fun RowsPaymentCard(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTexts: PlaceholderTexts = PlaceholderDefaults.texts(),
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    onDataChange: (PaymentCardData) -> Unit = {},
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
