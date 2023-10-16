package com.evervault.sdk.input.utils

internal class CreditCardExpirationDateCursorCalculator {

    internal fun newCursorPosition(
        enteredText: String,
        formattedText: String,
        currentCursorPosition: Int,
        lastCursorPosition: Int
    ) = when {
        formattedText.length > enteredText.length -> currentCursorPosition + 1 // Formatting the entered text added the separator, move the cursor to the year digit
        formattedText.length == enteredText.length && lastCursorPosition == 1 && currentCursorPosition == 2 -> currentCursorPosition + 1 // Edited the second month digit and formatting succeeded, move the cursor to the year digit
        enteredText.length > formattedText.length -> calculateNewCursorPositionAfterDigitsAdded(
            formattedText = formattedText,
            currentCursorPosition = currentCursorPosition,
            lastCursorPosition = lastCursorPosition
        )
        else -> currentCursorPosition
    }

    private fun calculateNewCursorPositionAfterDigitsAdded(
        formattedText: String,
        currentCursorPosition: Int,
        lastCursorPosition: Int
    ) = when {
        // Edited the month digit, but formatting failed (wrong digit),
        // return cursor to the previous month digit to re-enter it again
        currentCursorPosition < 3 && currentCursorPosition - lastCursorPosition == 1 && formattedText.length < 5 -> currentCursorPosition - 1
        // The new digit was correct and formatting succeeded,
        // move cursor to next position but do not move it after the year's second digit
        currentCursorPosition in 2..3 -> currentCursorPosition + 1
        else -> currentCursorPosition
    }
}
