package com.dangerfield.ui.components.checkbox

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    androidx.compose.material3.Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors.toMaterial(),
        interactionSource = interactionSource

    )
}

@Composable
@Preview
private fun Unchecked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Checkbox(checked = false, onCheckedChange = {})
    }
}

@Composable
@Preview
private fun Checked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Checkbox(checked = true, onCheckedChange = {})
    }
}

@Composable
@Preview
private fun DisabledChecked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Checkbox(
            checked = true,
            enabled = false,
            onCheckedChange = {}
        )
    }
}

@Composable
@Preview
private fun DisabledUnChecked() {
    com.dangerfield.libraries.ui.preview.Preview {
        Checkbox(
            checked = false,
            enabled = false,
            onCheckedChange = {}
        )
    }
}


object CheckboxDefaults {
    @Composable
    fun colors() = CheckboxColors(
        checkedCheckmarkColor = PodawanTheme.colors.background.color,
        uncheckedCheckmarkColor = Color.Transparent,
        checkedBoxColor = PodawanTheme.colors.surfacePrimary.color,
        uncheckedBoxColor = Color.Transparent,
        disabledCheckedBoxColor = PodawanTheme.colors.textDisabled.color,
        disabledUncheckedBoxColor = PodawanTheme.colors.textDisabled.color,
        disabledIndeterminateBoxColor = PodawanTheme.colors.textDisabled.color,
        checkedBorderColor = PodawanTheme.colors.surfacePrimary.color,
        uncheckedBorderColor = PodawanTheme.colors.border.color,
        disabledBorderColor = PodawanTheme.colors.textDisabled.color,
        disabledUncheckedBorderColor = PodawanTheme.colors.textDisabled.color,
        disabledIndeterminateBorderColor = PodawanTheme.colors.textDisabled.color
    )
}

@Immutable
data class CheckboxColors (
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBoxColor: Color,
    val uncheckedBoxColor: Color,
    val disabledCheckedBoxColor: Color,
    val disabledUncheckedBoxColor: Color,
    val disabledIndeterminateBoxColor: Color,
    val checkedBorderColor: Color,
    val uncheckedBorderColor: Color,
    val disabledBorderColor: Color,
    val disabledUncheckedBorderColor: Color,
    val disabledIndeterminateBorderColor: Color
)

private fun CheckboxColors.toMaterial() = androidx.compose.material3.CheckboxColors(
    checkedCheckmarkColor = checkedCheckmarkColor,
    uncheckedCheckmarkColor = uncheckedCheckmarkColor,
    checkedBoxColor = checkedBoxColor,
    uncheckedBoxColor = uncheckedBoxColor,
    disabledCheckedBoxColor = disabledCheckedBoxColor,
    disabledUncheckedBoxColor = disabledUncheckedBoxColor,
    disabledIndeterminateBoxColor = disabledIndeterminateBoxColor,
    checkedBorderColor = checkedBorderColor,
    uncheckedBorderColor = uncheckedBorderColor,
    disabledBorderColor = disabledBorderColor,
    disabledUncheckedBorderColor = disabledUncheckedBorderColor,
    disabledIndeterminateBorderColor = disabledIndeterminateBorderColor
)