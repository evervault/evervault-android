package com.evervault.sdk.input.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.evervault.sdk.input.ui.modifier.autofillBoundingBox
import com.evervault.sdk.input.ui.modifier.autofillOnFocusChange

/**
 * Input field that accepts composables for the placeholder and labels, so the user can modify and provide them as they need.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun CustomTextField(
    state: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(),
    onNext: (() -> Unit)? = null,
    autofillType: AutofillType,
    cursorBrush: Brush? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(
        autofillTypes = listOf(autofillType),
        onFill = {
            state.value = TextFieldValue(state.value.text + it)
        }
    )
    LocalAutofillTree.current.plusAssign(autofillNode)
    val defaultOrPassedBrush = setDefaultCursorBrush(cursorBrush)
    BasicTextField(
        value = state.value,
        onValueChange = { state.value = it },
        modifier = modifier
            .autofillBoundingBox(autofillNode)
            .autofillOnFocusChange(autofill, autofillNode),
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
        cursorBrush = defaultOrPassedBrush,
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

@Composable
private fun setDefaultCursorBrush(cursorBrush: Brush?): Brush {
    cursorBrush?.let { return it }
    return if(isSystemInDarkTheme()) {
        SolidColor(Color.White)
    } else {
        SolidColor(Color.Black)
    }
}
