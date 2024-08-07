package com.dangerfield.ui.components.text

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun AsteriskText(text: @Composable () -> Unit) {
    Row {
        text()
        HorizontalSpacerD200()
        Text(
            text = "*",
            typography = PodawanTheme.typography.Display.D800,
            colorResource = ColorResource.CherryPop700
        )
    }
}