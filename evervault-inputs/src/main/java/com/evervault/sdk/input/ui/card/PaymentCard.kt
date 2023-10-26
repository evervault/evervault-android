package com.evervault.sdk.input.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.evervault.sdk.input.mapper.PaymentCardDataMapper
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.model.PaymentCardData as OldPaymentCardData

/**
 * The PaymentCardComponent represents the whole card input form with default styles and inline layout.
 *
 * [Default inlined content image](https://github.com/evervault/evervault-android/blob/main/inline.png?raw=true)
 *
 * The user is allowed to customize the whole layout by providing its own content as a trailing lambda.
 *
 * Samples of the default (inline), rows and custom layouts:
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardDefaultLayoutPreview
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardComponentRowsLayoutPreview
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardCustomLayoutPreview
 *
 * A common use case is to use the default parameters with default parameters.
 * The user can customize them to provide more a look and feel more close to the rest of their app.
 * For more information, see [Inputs docs](https://docs.evervault.com/sdks/android#inputs)
 *
 * @param modifier the [Modifier] to be applied to the layout
 * @param textStyle the [TextStyle] to be applied to the input texts
 * @param placeholderTextStyle the [TextStyle] to be applied to the input texts placeholders
 * @param onDataChange the listener to be invoked when the underlying card data changes
 * @param content a lambda to provide the user content layout
 */
@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    onDataChange: (PaymentCardData) -> Unit = {},
    content: @Composable PaymentCardInputScope.(modifier: Modifier) -> Unit,
) {
    val paymentCardDataMapper = PaymentCardDataMapper()
    val mapCardDataOldAndReturnResult: (OldPaymentCardData) -> Unit = { paymentCardDataOld ->
        onDataChange(paymentCardDataMapper.apply(paymentCardDataOld))
    }

    PaymentCardInput(
        modifier = modifier,
        textStyle = textStyle,
        placeholderTextStyle = placeholderTextStyle,
        layout = content,
        onDataChange = mapCardDataOldAndReturnResult
    )
}
