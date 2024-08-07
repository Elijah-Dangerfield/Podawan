package com.dangerfield.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.ui.components.text.ProvideTextConfig
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun ProgressRow(
    progressPercent: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .background(PodawanTheme.colors.surfaceDisabled.color),
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progressPercent)
                .background(PodawanTheme.colors.surfacePrimary.color)
        ) {}

        Box(
            modifier = Modifier
        ) {
            ProvideTextConfig(
                typography = PodawanTheme.typography.Body.B600,
                color = PodawanTheme.colors.onSurfacePrimary
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ProgressRowPreview() {
    com.dangerfield.libraries.ui.preview.Preview(
    ) {
        ProgressRow(progressPercent = 0.5f) {
            Text("Hello")
        }
    }
}