package com.evervault.sdk.input.ui.modifier

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.focus.onFocusChanged

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.autofillOnFocusChange(autofill: Autofill?, autofillNode: AutofillNode) =
    this.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }

