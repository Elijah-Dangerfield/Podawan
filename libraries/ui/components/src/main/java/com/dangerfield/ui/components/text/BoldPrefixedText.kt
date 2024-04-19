package com.dangerfield.ui.components.text

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.makeBold
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.typography.TypographyResource

@Composable
fun BoldPrefixedText(
    modifier: Modifier = Modifier,
    boldText: String,
    regularText: String,
    textAlign: TextAlign = TextAlign.Start,
    typography: TypographyResource = PodawanTheme.typography.Body.B800
) {
    val string = "$boldText $regularText".makeBold(boldText)

    Row(modifier = modifier) {
       Text(
           textAlign = textAlign,
           text = string,
           typography = typography
       )
    }
}

@Preview
@Composable
fun BoldPrefixedTextPreview() {
    com.dangerfield.libraries.ui.preview.Preview {
        BoldPrefixedText(
            boldText = "Role: ",
            regularText = "Spy",
            typography = PodawanTheme.typography.Body.B800
        )
    }
}