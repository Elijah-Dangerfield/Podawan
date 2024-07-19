package com.dangerfield.ui.components.icon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.ui.components.Surface
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.thenIf

@Composable
fun CircleIcon(
    icon: PodawanIcon,
    iconSize: IconSize,
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    backgroundColor: ColorResource = PodawanTheme.colors.surfacePrimary,
    contentColor: ColorResource = PodawanTheme.colors.onSurfacePrimary,
    elevation: Elevation = Elevation.None,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        contentPadding = PaddingValues(padding),
        elevation = elevation,
        radius = Radii.Round,
        modifier = modifier
            .thenIf(onClick != null) {
                clickable { onClick?.invoke() }
            }
    ) {
        Icon(
            podawanIcon = icon,
            iconSize = iconSize
        )
    }
}

@Preview
@Composable
private fun CircularIconPreview() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = false) {
        CircleIcon(
            icon = PodawanIcon.Android("Test"),
            iconSize = IconSize.Large,
            padding = Dimension.D400,
            backgroundColor = PodawanTheme.colors.background,
            contentColor = PodawanTheme.colors.onBackground
        )
    }
}
