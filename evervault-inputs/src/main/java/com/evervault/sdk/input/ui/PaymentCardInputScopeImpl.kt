package com.evervault.sdk.input.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.defaults.placeholder.model.PlaceholderTextsDefaults

internal class PaymentCardInputScopeImpl(
    private val textStyle: TextStyle,
    private val placeholderTextStyle: TextStyle,
    private val cardImageResource: Int,
    private val creditCardNumber: MutableState<TextFieldValue>,
    private val creditCardRequester: FocusRequester,
    private val expiryDate: MutableState<TextFieldValue>,
    private val expiryRequester: FocusRequester,
    private val cvc: MutableState<TextFieldValue>,
    private val cvcRequester: FocusRequester,
) : PaymentCardInputScope {

    @Composable
    override fun CardImage() {
        CardImage(modifier = Modifier)
    }

    @Composable
    override fun CardImage(modifier: Modifier) {
        Icon(
            painter = painterResource(id = cardImageResource), contentDescription = "",
            modifier = modifier,
            tint = Color.Unspecified
        )
    }

    @Composable
    override fun CardNumberField() {
        CardNumberField(modifier = Modifier)
    }

    @Composable
    override fun CardNumberField(modifier: Modifier) {
        CardNumberField(modifier = modifier, options = PaymentCardInputScope.TextFieldOptions())
    }

    @Composable
    override fun CardNumberField(
        modifier: Modifier,
        options: PaymentCardInputScope.TextFieldOptions
    ) {
        CardNumberField(
            modifier = modifier,
            options = options,
            placeholder = PlaceholderTextsDefaults.CreditCardText
        )
    }

    @Composable
    override fun CardNumberField(
        modifier: Modifier,
        options: PaymentCardInputScope.TextFieldOptions,
        placeholder: String
    ) {
        CustomTextField(
            state = creditCardNumber,
            placeholder = placeholder,
            modifier = modifier.focusRequester(creditCardRequester),
            options = options,
            onNext = { expiryRequester.requestFocus() },
        )
    }

    @Composable
    override fun CardNumberField(
        modifier: Modifier,
        label: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    ) {
        CustomTextField(
            state = creditCardNumber,
            modifier = modifier.focusRequester(creditCardRequester),
            label = label,
            placeholder = placeholder,
            textStyle = textStyle,
            textFieldColors = textFieldColors,
            onNext = { expiryRequester.requestFocus() },
        )
    }

    @Composable
    override fun ExpiryField() {
        ExpiryField(modifier = Modifier)
    }

    @Composable
    override fun ExpiryField(modifier: Modifier) {
        ExpiryField(modifier = modifier, options = PaymentCardInputScope.TextFieldOptions())
    }

    @Composable
    override fun ExpiryField(modifier: Modifier, options: PaymentCardInputScope.TextFieldOptions) {
        ExpiryField(
            modifier = modifier,
            options = options,
            placeholder = PlaceholderTextsDefaults.ExpirationDateText
        )
    }

    @Composable
    override fun ExpiryField(
        modifier: Modifier,
        options: PaymentCardInputScope.TextFieldOptions,
        placeholder: String
    ) {
        CustomTextField(
            state = expiryDate,
            placeholder = placeholder,
            modifier = modifier.focusRequester(expiryRequester),
            options = options,
            onNext = { cvcRequester.requestFocus() },
        )
    }

    @Composable
    override fun ExpiryField(
        modifier: Modifier,
        label: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    ) {
        CustomTextField(
            state = expiryDate,
            modifier = modifier.focusRequester(expiryRequester),
            label = label,
            placeholder = placeholder,
            textStyle = textStyle,
            textFieldColors = textFieldColors,
            onNext = { cvcRequester.requestFocus() },
        )
    }

    @Composable
    override fun CVCField() {
        CVCField(
            modifier = Modifier
        )
    }

    @Composable
    override fun CVCField(modifier: Modifier) {
        CVCField(modifier = modifier, options = PaymentCardInputScope.TextFieldOptions())
    }

    @Composable
    override fun CVCField(modifier: Modifier, options: PaymentCardInputScope.TextFieldOptions) {
        CVCField(
            modifier = modifier,
            options = options,
            placeholder = PlaceholderTextsDefaults.CvcText
        )
    }

    @Composable
    override fun CVCField(
        modifier: Modifier,
        options: PaymentCardInputScope.TextFieldOptions,
        placeholder: String
    ) {
        CustomTextField(
            state = cvc,
            placeholder = placeholder,
            modifier = modifier.focusRequester(cvcRequester),
            options = options,
        )
    }

    @Composable
    override fun CVCField(
        modifier: Modifier,
        label: (@Composable () -> Unit)?,
        placeholder: (@Composable () -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    ) {
        CustomTextField(
            state = cvc,
            modifier = modifier.focusRequester(cvcRequester),
            label = label,
            placeholder = placeholder,
            textStyle = textStyle,
            textFieldColors = textFieldColors
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CustomTextField(
        state: MutableState<TextFieldValue>,
        placeholder: String,
        modifier: Modifier = Modifier,
        options: PaymentCardInputScope.TextFieldOptions,
        onNext: (() -> Unit)? = null,
    ) {
        BasicTextField(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = modifier,
            textStyle = options.textStyle.invoke(textStyle),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
                imeAction = if (onNext == null) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
            ),
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = state.value.text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeholder,
                            style = options.textStyle.invoke(placeholderTextStyle),
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                )
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CustomTextField(
        state: MutableState<TextFieldValue>,
        modifier: Modifier = Modifier,
        label: (@Composable () -> Unit)? = null,
        placeholder: (@Composable () -> Unit)? = null,
        textStyle: TextStyle = LocalTextStyle.current,
        textFieldColors: TextFieldColors = TextFieldDefaults.colors(),
        onNext: (() -> Unit)? = null,
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = modifier,
            textStyle = textStyle,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
                imeAction = if (onNext == null) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
            ),
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = state.value.text,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    label = label,
                    placeholder = placeholder,
                    colors = textFieldColors,
                    contentPadding = PaddingValues(0.dp),
                )
            }
        )
    }
}
