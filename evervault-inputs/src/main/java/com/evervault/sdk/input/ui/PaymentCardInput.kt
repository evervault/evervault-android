package com.evervault.sdk.input.ui

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import com.evervault.sdk.Evervault
import com.evervault.sdk.input.model.*
import com.evervault.sdk.input.model.expiry
import com.evervault.sdk.input.utils.CreditCardExpirationDateCursorCalculator
import com.evervault.sdk.input.utils.CreditCardExpirationDateFormatter
import com.evervault.sdk.input.utils.CreditCardValidator
import com.evervault.sdk.inputs.R

@Deprecated(message = "Use the InlinePaymentCard, RowsPaymentCard or PaymentCard in com.evervault.sdk.input.ui.card instead")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentCardInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTextStyle: TextStyle = textStyle.copy(color = MaterialTheme.colorScheme.secondary),
    layout: @Composable PaymentCardInputScope.(modifier: Modifier) -> Unit = inlinePaymentCardInputLayout(),
    onDataChange: (PaymentCardData) -> Unit = {},
    enabledFields: List<CardFields> = listOf(CardFields.CARD_NUMBER, CardFields.EXPIRY_DATE, CardFields.CVC)
) {
    val creditCardNumber = remember { mutableStateOf(TextFieldValue("")) }
    val expiryDate = remember { mutableStateOf(TextFieldValue("")) }
    val cvc = remember { mutableStateOf(TextFieldValue("")) }

    var expiryDateLastCursorPosition by remember { mutableStateOf(0) }

    val (creditCardNumberRequester, expiryDateRequester, cvcRequester) = remember { FocusRequester.createRefs() }

    var rawCardData by remember { mutableStateOf(PaymentCardData()) }
    var cardData by remember { mutableStateOf(PaymentCardData()) }

    val cardImageResource = cardData.card.type?.let { type ->
        if (cardData.isPotentiallyValid || CreditCardValidator(rawCardData.card.number).actualType?.let {
                CreditCardValidator.isValidCvc(rawCardData.card.cvc, it)
            } == false) {
            when (type) {
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
    val expiryDateCursorCalculator by lazy { CreditCardExpirationDateCursorCalculator() }
    val expiryDateFormatter by lazy { CreditCardExpirationDateFormatter() }

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
                    number = if (rawCardData.card.number.isBlank()) {
                        ""
                    } else {
                        Evervault.shared.encrypt(
                            rawCardData.card.number.replace(" ", "")
                        ) as? String ?: ""
                    }
                )
            )
        }
    }

    LaunchedEffect(rawCardData.card.cvc) {
        updateCardData {
            copy(
                card = card.copy(
                    cvc = if (rawCardData.card.cvc.isBlank()) {
                        ""
                    } else {
                        Evervault.shared.encrypt(rawCardData.card.cvc) as? String ?: ""
                    }
                )
            )
        }
    }

    LaunchedEffect(rawCardData.expiry) {
        updateCardData()
    }

    LaunchedEffect(creditCardNumber.value) {
        val numberText = creditCardNumber.value.text
        rawCardData = rawCardData.updateNumber(numberText, enabledFields)

        val selection = creditCardNumber.value.selection
        var cursorPosition = selection.start

        if (rawCardData.card.number.length > numberText.length) {
            cursorPosition++
        }

        creditCardNumber.value = TextFieldValue(
            text = rawCardData.card.number,
            selection = TextRange(cursorPosition)
        )
    }

    LaunchedEffect(cvc.value) {
        rawCardData = rawCardData.updateCvc(cvc.value.text, enabledFields)
        cvc.value = cvc.value.copy(text = rawCardData.card.cvc)
    }

    LaunchedEffect(expiryDate.value) {
        val enteredText = expiryDate.value.text
        val formattedExpiry = expiryDateFormatter.format(enteredText)
        rawCardData = rawCardData.updateExpiry(formattedExpiry, enabledFields)

        val newCursorPosition = expiryDateCursorCalculator.newCursorPosition(
            enteredText = enteredText,
            formattedText = formattedExpiry,
            currentCursorPosition = expiryDate.value.selection.start,
            lastCursorPosition = expiryDateLastCursorPosition
        )
        expiryDateLastCursorPosition = newCursorPosition

        expiryDate.value = TextFieldValue(
            text = formattedExpiry,
            selection = TextRange(newCursorPosition)
        )
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
            cvcRequester = cvcRequester
        ),
        modifier
    )
}

