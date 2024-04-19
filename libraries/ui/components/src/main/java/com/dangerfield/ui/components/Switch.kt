package com.dangerfield.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.ui.preview.Preview
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = PodawanTheme.colors.onSurfacePrimary.color,
            uncheckedTrackColor = PodawanTheme.colors.surfacePrimary.color,
            uncheckedBorderColor = PodawanTheme.colors.onSurfacePrimary.color,
            checkedThumbColor = PodawanTheme.colors.onAccent.color,
            checkedTrackColor = PodawanTheme.colors.accent.color,
            checkedBorderColor = PodawanTheme.colors.accent.color,
            disabledCheckedBorderColor = PodawanTheme.colors.onSurfaceDisabled.color,
            disabledUncheckedBorderColor = PodawanTheme.colors.onSurfaceDisabled.color,
            disabledCheckedThumbColor = PodawanTheme.colors.onSurfaceDisabled.color,
            disabledUncheckedThumbColor = PodawanTheme.colors.onSurfaceDisabled.color,
            disabledCheckedTrackColor = PodawanTheme.colors.surfaceDisabled.color,
            disabledUncheckedTrackColor = PodawanTheme.colors.surfaceDisabled.color
        )
    )
}

@Composable
@Preview
private fun Unchecked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Switch(checked = false, onCheckedChange = {})
    }
}

@Composable
@Preview
private fun Checked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Switch(checked = true, onCheckedChange = {})
    }
}

@Composable
@Preview
private fun CheckedDisabled() {
    com.dangerfield.libraries.ui.preview.Preview {
        Switch(
            checked = true,
            onCheckedChange = {},
            enabled = false
        )
    }
}

@Composable
@Preview
private fun UncheckedDisabled() {
    com.dangerfield.libraries.ui.preview.Preview {
        Switch(
            checked = false,
            onCheckedChange = {},
            enabled = false
        )
    }
}

