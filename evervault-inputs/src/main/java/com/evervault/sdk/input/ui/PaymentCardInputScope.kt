package com.evervault.sdk.input.ui

import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle

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
    fun CardNumberField(modifier: Modifier, options: TextFieldOptions, placeholder: String)

    @Composable
    fun CardNumberField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    )

    @Composable
    fun CardNumberField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
        cursorBrush: Brush?
    )

    @Composable
    fun CardNumberField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
        cursorBrush: Brush?,
        onNext: (() -> Unit)?
    )

    @Composable
    fun ExpiryField()

    @Composable
    fun ExpiryField(modifier: Modifier)

    @Composable
    fun ExpiryField(modifier: Modifier, options: TextFieldOptions)

    @Composable
    fun ExpiryField(modifier: Modifier, options: TextFieldOptions, placeholder: String)

    @Composable
    fun ExpiryField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    )

    @Composable
    fun ExpiryField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
        cursorBrush: Brush?,
    )

    @Composable
    fun CVCField()

    @Composable
    fun CVCField(modifier: Modifier)

    @Composable
    fun CVCField(modifier: Modifier, options: TextFieldOptions)

    @Composable
    fun CVCField(modifier: Modifier, options: TextFieldOptions, placeholder: String)

    @Composable
    fun CVCField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
    )

    @Composable
    fun CVCField(
        modifier: Modifier,
        label: @Composable() (() -> Unit)?,
        placeholder: @Composable() (() -> Unit)?,
        textStyle: TextStyle,
        textFieldColors: TextFieldColors,
        cursorBrush: Brush?,
    )
}
