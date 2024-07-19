package com.dangerfield.ui.components.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun CircleCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(PodawanTheme.colors.onBackground.color)
            .clipToBounds()
            .bounceClick(
                enabled = enabled,
                mutableInteractionSource = interactionSource
            ) { onCheckedChange?.invoke(!checked) },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Box(
            modifier =
            Modifier
                .clip(CircleShape)
                .background(PodawanTheme.colors.background.color)
                .fillMaxSize(0.8f),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            if (checked) {
                Box(
                    modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(PodawanTheme.colors.onBackground.color)
                        .fillMaxSize(0.8f)
                )
            }
        }
    }
}

@Composable
@Preview
private fun Unchecked() {
    com.dangerfield.libraries.ui.preview.Preview {
        var isChecked by remember { mutableStateOf(false) }
        CircleCheckbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
            }
        )
    }
}

@Composable
@Preview
private fun Checked() {
    com.dangerfield.libraries.ui.preview.Preview {
        var isChecked by remember { mutableStateOf(true) }
        CircleCheckbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
            }
        )
    }
}