package com.dangerfield.features.settings.internal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.rememberRipple

@Composable
fun SettingsOption(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: PodawanIcon? = null,
    trailingIcon: PodawanIcon? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(indication = rememberRipple()) { onClick() }
            .padding(vertical = Dimension.D500,),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(podawanIcon = it, iconSize = IconSize.Large)
            Spacer(modifier = Modifier.width(Dimension.D500))
        }
        text()
        Spacer(modifier = Modifier.weight(1f))
        trailingIcon?.let { Icon(podawanIcon = it, iconSize = IconSize.Large) }
    }
}

@Composable
fun SettingsOption(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: PodawanIcon? = null,
    trailingIcon: PodawanIcon? = null,
) {
    SettingsOption(
        text = { Text(text = text) },
        onClick = onClick,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}


@Composable
@Preview
fun PreviewSettingsOption() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = true) {
        SettingsOption(
            leadingIcon = PodawanIcon.Settings("Example"),
            text = "Example",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun PreviewSettingsOptionTrailing() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = true) {
        SettingsOption(
            trailingIcon = PodawanIcon.ChevronRight("Example"),
            text = "Example",
            onClick = {}
        )
    }
}