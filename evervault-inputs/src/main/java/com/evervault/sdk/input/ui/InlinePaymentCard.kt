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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.defaults.PlaceholderDefaults
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTexts
import com.evervault.sdk.input.model.card.PaymentCardData

/**
 * Represents the whole card input form with default styles and inline layout.
 *
 * [Default inlined content image](https://github.com/evervault/evervault-android/blob/main/inline.png?raw=true)
 *
 * Sample:
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardComponentDefaultLayoutPreview
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
fun InlinePaymentCard(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTexts: PlaceholderTexts = PlaceholderDefaults.texts(),
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    onDataChange: (PaymentCardData) -> Unit = {},
) {
    PaymentCardComponent(
        modifier = modifier,
        textStyle = textStyle,
        placeholderTextStyle = placeholderTextStyle,
        onDataChange = onDataChange
    ) {
        InlineLayout(
            modifier = it,
            placeholderTexts = placeholderTexts,
        )
    }
}

@Composable
private fun PaymentCardInputScope.InlineLayout(
    modifier: Modifier,
    placeholderTexts: PlaceholderTexts,
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

        CardNumberField(
            modifier = Modifier.weight(0.66f, true),
            options = PaymentCardInputScope.TextFieldOptions(),
            placeholder = placeholderTexts.creditCardText
        )

        ExpiryField(
            modifier = Modifier.weight(0.20f, true),
            options = PaymentCardInputScope.TextFieldOptions(),
            placeholder = placeholderTexts.expirationDateText
        )

        CVCField(
            modifier = Modifier.weight(0.14f, true),
            options = PaymentCardInputScope.TextFieldOptions(),
            placeholder = placeholderTexts.cvcText
        )
    }
}
