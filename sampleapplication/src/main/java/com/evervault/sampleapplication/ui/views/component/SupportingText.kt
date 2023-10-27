package com.evervault.sampleapplication.ui.views.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle

@Composable
internal fun SupportingText(text: String) {
    Text(
        text = text,
        fontStyle = FontStyle.Italic
    )
}
