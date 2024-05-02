package com.evervault.sdk.input.ui.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.evervault.sdk.input.mapper.PaymentCardDataMapper
import com.evervault.sdk.input.model.CardFields
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.ui.PaymentCardInput
import com.evervault.sdk.input.ui.PaymentCardInputScope
import com.evervault.sdk.input.model.PaymentCardData as OldPaymentCardData

/**
 * Represents the customizable card input form with no predefined layout.
 *
 * The user is allowed to customize the whole layout by providing its own content in a trailing lambda.
 *
 * Sample:
 * @sample com.evervault.sdk.input.ui.sample.PaymentCardWithCustomizedLayoutSample
 *
 * A common use case is to provide custom layout when the standard [InlinePaymentCard] and [RowsPaymentCard]
 * do not provide the layout that matches the users design system.
 * For more information, see [Styling docs](https://docs.evervault.com/sdks/android#styling)
 *
 * @param modifier the [Modifier] to be applied to the layout
 * @param textStyle the [TextStyle] to be applied to the input texts
 * @param placeholderTextStyle the [TextStyle] to be applied to the input texts placeholders
 * @param onDataChange the listener to be invoked when the underlying card data changes
 * @param content a lambda to provide the user's own layout
 */
@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    onDataChange: (PaymentCardData) -> Unit = {},
    enabledFields: List<CardFields> = listOf(CardFields.CARD_NUMBER, CardFields.EXPIRY_DATE, CardFields.CVC),
    content: @Composable PaymentCardInputScope.(modifier: Modifier) -> Unit,
) {
    /**
    Temporarily mapping the  to map [OldPaymentCardData] to the new [PaymentCardData]
    It will be removed when we remove the old constructor for [PaymentCardInput], change its
    internal implementation to return already mapped data, and all the data logic has been extracted from it.
     */
    val paymentCardDataMapper = PaymentCardDataMapper()
    val mapCardDataOldAndReturnResult: (OldPaymentCardData) -> Unit = { paymentCardDataOld ->
        onDataChange(paymentCardDataMapper.apply(paymentCardDataOld))
    }

    PaymentCardInput(
        modifier = modifier,
        textStyle = textStyle,
        placeholderTextStyle = placeholderTextStyle,
        layout = content,
        onDataChange = mapCardDataOldAndReturnResult,
        enabledFields = enabledFields
    )
}
