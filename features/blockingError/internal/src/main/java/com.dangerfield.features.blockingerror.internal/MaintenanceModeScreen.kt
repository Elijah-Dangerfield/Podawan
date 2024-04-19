package com.dangerfield.features.blockingerror.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.dictionary.dictionaryString
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.oddoneoout.features.blockingerror.internal.R

@Composable
fun MaintenanceModeScreen() {
    Screen { paddingValues ->
        Column(
            Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.10f))
            Text(
                text = dictionaryString(R.string.blockingError_screen_header),
                typography = PodawanTheme.typography.Display.D1000,
                modifier = Modifier.padding(horizontal = Dimension.D500),
                textAlign = TextAlign.Center
            )
            Text(
                text = dictionaryString(R.string.blockingError_maintenenceMode_body),
                typography = PodawanTheme.typography.Body.B700,
                modifier = Modifier.padding(horizontal = Dimension.D500),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview
private fun PreviewMaintenanceModeScreen() {
    com.dangerfield.libraries.ui.preview.Preview {
        MaintenanceModeScreen()
    }
}
