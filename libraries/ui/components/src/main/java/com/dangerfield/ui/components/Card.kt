package com.dangerfield.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun Card(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = PodawanTheme.colors.surfacePrimary,
        contentColor = PodawanTheme.colors.onSurfacePrimary,
        onClick = onClick ?: {},
        bounceScale = if (onClick != null) 0.9f else 1f,
        elevation = Elevation.Button,
        radius = Radii.Card,
        contentPadding = PaddingValues(Dimension.D1000)
    ) {
        content()
    }
}
