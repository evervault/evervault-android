package com.evervault.sdk.input.ui.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.evervault.sdk.input.model.card.PaymentCardData
import com.evervault.sdk.input.ui.PaymentCardInputScope
import com.evervault.sdk.input.ui.PaymentCardInputScopeImpl
import com.evervault.sdk.inputs.R

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
    content: @Composable PaymentCardInputScope.(modifier: Modifier) -> Unit
) {
    // Initialize the state and other dependencies needed for the PaymentCardInputScope
    val cardNumber = remember { mutableStateOf(TextFieldValue()) }
    val expiryDate = remember { mutableStateOf(TextFieldValue()) }
    val cvc = remember { mutableStateOf(TextFieldValue()) }
    val cardNumberRequester = remember { FocusRequester() }
    val expiryRequester = remember { FocusRequester() }
    val cvcRequester = remember { FocusRequester() }


    // Create an instance of PaymentCardInputScope
    val paymentCardInputScope = PaymentCardInputScopeImpl(
        textStyle = textStyle,
        placeholderTextStyle = placeholderTextStyle,
        cardImageResource = R.drawable.ic_credit_card, // Replace with actual resource ID
        creditCardNumber = cardNumber,
        creditCardRequester = cardNumberRequester,
        expiryDate = expiryDate,
        expiryRequester = expiryRequester,
        cvc = cvc,
        cvcRequester = cvcRequester
    )

    // Use the content lambda to allow the caller to define the custom layout
    paymentCardInputScope.content(modifier)

}
