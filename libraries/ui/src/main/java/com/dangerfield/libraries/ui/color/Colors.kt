package com.dangerfield.libraries.ui.color

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.LocalContentColor

@Immutable
@Suppress("LongParameterList")
interface Colors {

    val accent: ColorResource
    val onAccent: ColorResource

    /* Backgrounds */
    val shadow: ColorResource
    val background: ColorResource
    val backgroundOverlay: ColorResource
    val onBackground: ColorResource
    val border: ColorResource
    val borderDisabled: ColorResource

    /* Texts */
    val text: ColorResource
    val textSecondary: ColorResource
    val textDisabled: ColorResource
    val textWarning: ColorResource

    /* Surfaces */
    val surfacePrimary: ColorResource
    val onSurfacePrimary: ColorResource
    val surfaceSecondary: ColorResource
    val onSurfaceSecondary: ColorResource
    val surfaceDisabled: ColorResource
    val onSurfaceDisabled: ColorResource

}

val defaultColors = object : Colors {
    override val accent = ColorResource.MintyFresh300
    override val onAccent = ColorResource.Black800
    override val shadow = ColorResource.Black800
    override val textDisabled = ColorResource.Black600
    override val textWarning = ColorResource.CherryPop700
    override val surfacePrimary = ColorResource.White900
    override val surfaceDisabled = ColorResource.Black600
    override val onSurfacePrimary = ColorResource.Black900
    override val surfaceSecondary = ColorResource.Black700
    override val onSurfaceSecondary = ColorResource.White900
    override val onSurfaceDisabled = ColorResource.Black700
    override val background = ColorResource.Black900
    override val onBackground = ColorResource.White900
    override val border = ColorResource.White900
    override val borderDisabled = ColorResource.Black600
    override val text = ColorResource.White900
    override val backgroundOverlay = ColorResource.Black900_A_70
    override val textSecondary = ColorResource.Black500
}

@Composable
fun PreviewColorSwatch(colors: Colors) {
    val colors = colors.let {
        // map of color resource to name
        listOf(
            it.accent to "accent",
            it.onAccent to "onAccent",
            it.shadow to "shadow",
            it.background to "background",
            it.backgroundOverlay to "backgroundOverlay",
            it.onBackground to "onBackground",
            it.border to "border",
            it.borderDisabled to "borderDisabled",
            it.text to "text",
            it.textDisabled to "textDisabled",
            it.textWarning to "textWarning",
            it.surfacePrimary to "surfacePrimary",
            it.surfaceSecondary to "surfaceSecondary",
            it.surfaceDisabled to "surfaceDisabled",
            it.onSurfacePrimary to "onSurfacePrimary",
            it.onSurfaceSecondary to "onSurfaceSecondary",
            it.onSurfaceDisabled to "onSurfaceDisabled",
        )
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(colors) { (color, name) ->
            ColorCard(
                color,
                title = name,
                description = ""
            )
        }
    }
}

@Composable
fun ProvideContentColor(color: ColorResource, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides color,
        androidx.compose.material3.LocalContentColor provides color.color,
        content = content
    )
}

