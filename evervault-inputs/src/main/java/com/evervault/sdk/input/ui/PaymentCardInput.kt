package com.evervault.sdk.input.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.evervault.sdk.Evervault
import com.evervault.sdk.inputs.R
import com.evervault.sdk.input.model.CreditCardType
import com.evervault.sdk.input.model.PaymentCardData
import com.evervault.sdk.input.model.PaymentCardError
import com.evervault.sdk.input.model.expiry
import com.evervault.sdk.input.model.updateCvc
import com.evervault.sdk.input.model.updateExpiry
import com.evervault.sdk.input.model.updateNumber
import com.evervault.sdk.input.utils.CreditCardFormatter
import com.evervault.sdk.input.utils.CreditCardValidator
import com.evervault.sdk.input.utils.validNumberLength

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentCardInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTextStyle: TextStyle = textStyle.copy(
        color = MaterialTheme.colorScheme.secondary
    ),
    layout: @Composable PaymentCardInputScope.(modifier: Modifier) -> Unit = inlinePaymentCardInputLayout(),
    onDataChange: (PaymentCardData) -> Unit = {}
) {
    val creditCardNumber = remember { mutableStateOf(TextFieldValue("")) }
    val expiryDate = remember { mutableStateOf(TextFieldValue("")) }
    val cvc = remember { mutableStateOf(TextFieldValue("")) }

    var expiryTextLen by remember { mutableStateOf(0) }

    val (creditCardNumberRequester, expiryDateRequester, cvcRequester) = remember { FocusRequester.createRefs() }

    var rawCardData by remember { mutableStateOf(PaymentCardData()) }
    var cardData by remember { mutableStateOf(PaymentCardData()) }

    val cardImageResource = cardData.card.type?.let { type ->
        if (cardData.isPotentiallyValid || CreditCardValidator(rawCardData.card.number).actualType?.let { CreditCardValidator.isValidCvc(rawCardData.card.cvc, it)} == false) {
            when(type) {
                CreditCardType.AMEX -> R.drawable.amex
                CreditCardType.DINERS_CLUB -> R.drawable.dinersclub
                CreditCardType.DISCOVER -> R.drawable.discover
                CreditCardType.JCB -> R.drawable.jcb
                CreditCardType.ELO -> R.drawable.elo
                CreditCardType.HIPER -> R.drawable.hiper
                CreditCardType.HIPERCARD -> R.drawable.hipercard
                CreditCardType.MAESTRO -> R.drawable.maestro
                CreditCardType.MASTERCARD -> R.drawable.mastercard
                CreditCardType.MIR -> R.drawable.mir
                CreditCardType.UNION_PAY -> R.drawable.unionpay
                CreditCardType.VISA -> R.drawable.visa
            }
        } else R.drawable.errorcard
    } ?: R.drawable.unknowncard

    suspend fun updateCardData(update: suspend PaymentCardData.() -> PaymentCardData = { this }) {
        try {
            cardData = rawCardData
                .copy(
                    card = rawCardData.card.copy(
                        number = cardData.card.number,
                        cvc = cardData.card.cvc,
                    )
                )
                .update()
        } catch (e: Exception) {
            cardData = cardData.copy(
                error = PaymentCardError.EncryptionFailed(e.localizedMessage)
            )
        }
    }

    LaunchedEffect(rawCardData.card.number) {
        updateCardData {
            copy(
                card = card.copy(
                    number = if (rawCardData.card.number.isBlank()) "" else Evervault.shared.encrypt(rawCardData.card.number.replace(" ", "")) as? String ?: ""
                )
            )
        }
    }

    LaunchedEffect(rawCardData.card.cvc) {
        updateCardData {
            copy(
                card = card.copy(
                    cvc = if (rawCardData.card.cvc.isBlank()) "" else Evervault.shared.encrypt(rawCardData.card.cvc) as? String ?: ""
                )
            )
        }
    }

    LaunchedEffect(rawCardData.expiry) {
        updateCardData()
    }

    LaunchedEffect(creditCardNumber.value) {
        val numberText = creditCardNumber.value.text
        rawCardData = rawCardData.updateNumber(numberText)

        val selection = creditCardNumber.value.selection
        var cursorPosition = selection.start

        if (rawCardData.card.number.length > numberText.length) {
            cursorPosition++
        }

        creditCardNumber.value = TextFieldValue(
            text = rawCardData.card.number,
            selection = TextRange(cursorPosition)
        )

        val validator = CreditCardValidator(creditCardNumber.value.text)
        validator.actualType?.let { type ->
            if (validator.string.length == type.validNumberLength.last()) {
                expiryDateRequester.requestFocus()
            }
        }
    }

    LaunchedEffect(cvc.value) {
        rawCardData = rawCardData.updateCvc(cvc.value.text)
        cvc.value = cvc.value.copy(text = rawCardData.card.cvc)
    }

    LaunchedEffect(expiryDate.value) {
        var value = expiryDate.value.text
        if (expiryTextLen > value.length && value.length == 2) {
            // delete key used on '/'
            value = value.dropLast(1)
        }

        expiryTextLen = value.length
        val formattedExpiry = CreditCardFormatter.formatExpiryDate(value)
        rawCardData = rawCardData.updateExpiry(formattedExpiry)

        val selection = creditCardNumber.value.selection
        var cursorPosition = selection.start

        if (formattedExpiry.length > value.length) {
            cursorPosition++
        }

        expiryDate.value = TextFieldValue(
            text = formattedExpiry,
            selection = TextRange(cursorPosition)
        )

        if (formattedExpiry.length == 5) {
            cvcRequester.requestFocus()
        }
    }

    LaunchedEffect(cardData) {
        onDataChange(cardData)
    }

    layout(
        PaymentCardInputScopeImpl(
            textStyle = textStyle,
            placeholderTextStyle = placeholderTextStyle,
            cardImageResource = cardImageResource,
            creditCardNumber = creditCardNumber,
            creditCardRequester = creditCardNumberRequester,
            expiryDate = expiryDate,
            expiryRequester = expiryDateRequester,
            cvc = cvc,
            cvcRequester = cvcRequester,
        ),
        modifier = modifier
            .height(IntrinsicSize.Min)
    )
}

interface PaymentCardInputScope {

    data class TextFieldOptions(
        val textStyle: (TextStyle.() -> TextStyle) = { this },
    )

    @Composable
    fun CardImage()
    @Composable
    fun CardImage(modifier: Modifier)

    @Composable
    fun CardNumberField()
    @Composable
    fun CardNumberField(modifier: Modifier)
    @Composable
    fun CardNumberField(modifier: Modifier, options: TextFieldOptions)

    @Composable
    fun ExpiryField()
    @Composable
    fun ExpiryField(modifier: Modifier)
    @Composable
    fun ExpiryField(modifier: Modifier, options: TextFieldOptions)

    @Composable
    fun CVCField()
    @Composable
    fun CVCField(modifier: Modifier)
    @Composable
    fun CVCField(modifier: Modifier, options: TextFieldOptions)
}

private class PaymentCardInputScopeImpl(
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
    override fun CardNumberField(modifier: Modifier, options: PaymentCardInputScope.TextFieldOptions) {
        CustomTextField(
            state = creditCardNumber,
            placeholder = "4242 4242 4242 4242",
            modifier = modifier.focusRequester(creditCardRequester),
            options = options,
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
        CustomTextField(
            state = expiryDate,
            placeholder = "MM/YY",
            modifier = modifier.focusRequester(expiryRequester),
            options = options,
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
        CustomTextField(
            state = cvc,
            placeholder = "CVC",
            modifier = modifier.focusRequester(cvcRequester),
            options = options,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    private fun CustomTextField(
        state: MutableState<TextFieldValue>,
        placeholder: String,
        modifier: Modifier = Modifier,
        options: PaymentCardInputScope.TextFieldOptions,
        onNext: (() -> Unit)? = null,
    ) {
        val autofillNode = AutofillNode(
            autofillTypes = listOf(AutofillType.CreditCardNumber),
            onFill = { state.value = TextFieldValue(it) }
        )
        val autofill = LocalAutofill.current

        LocalAutofillTree.current += autofillNode
        BasicTextField(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = modifier.onGloballyPositioned {
                autofillNode.boundingBox = it.boundsInWindow()
            }.onFocusChanged { focusState ->
                autofill?.run {
                    if (focusState.isFocused) {
                        requestAutofillForNode(autofillNode)
                    } else {
                        cancelAutofillForNode(autofillNode)
                    }
                }
            },
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
}
