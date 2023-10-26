package com.evervault.sdk.input.ui.card

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
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.placeholder.PlaceholderDefaults
import com.evervault.sdk.input.model.placeholder.PlaceholderTexts
import com.evervault.sdk.input.ui.PaymentCard
import com.evervault.sdk.input.ui.PaymentCardInputScope

/**
 * Represents the card input form with with predefined inlined layout (all inputs are in one row).
 *
 * [Default inlined card image](https://raw.githubusercontent.com/evervault/evervault-android/main/inline.png)
 *
 * For simplicity of use, the layout is provided as is, and cannot be changed (besides standard modifiers,
 * text styles and placeholder texts).
 * To manually customize the layout appearance use the [PaymentCard] component
 *
 * Samples:
 * @sample com.evervault.sdk.input.ui.sample.InlinePaymentCardSample
 * @sample com.evervault.sdk.input.ui.sample.InlineCustomizedPaymentCardSample
 * @sample com.evervault.sdk.input.ui.sample.InlinePaymentCardWithAppliedThemeSample
 *
 * A common use case is to use it when no specific changes have to need to be applied to the layout,
 * but only basic customization like text and placeholder styles, placeholder texts and layout modifiers.
 * The layout is inlined in one row.
 * For more information, see [Styling docs](https://docs.evervault.com/sdks/android#styling)
 *
 * @param modifier the [Modifier] to be applied to the layout
 * @param textStyle the [TextStyle] to be applied to the input texts
 * @param placeholderTexts the [PlaceholderTexts] to be applied to the text input placeholders
 * @param placeholderTextStyle the [TextStyle] to be applied to the text input placeholders
 * @param onDataChange the listener to be invoked when the underlying card data changes
 */
@Composable
fun InlinePaymentCard(
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
