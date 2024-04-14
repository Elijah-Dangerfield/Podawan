package com.dangerfield.libraries.ui.color

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.LocalContentColor
import com.dangerfield.libraries.ui.Preview

@Immutable
@Suppress("LongParameterList")
class Colors internal constructor(

    val accent: ColorResource,
    val onAccent: ColorResource,

    /* Backgrounds */
    val shadow: ColorResource,
    val background: ColorResource,
    val backgroundOverlay: ColorResource,
    val onBackground: ColorResource,
    val border: ColorResource,
    val borderDisabled: ColorResource,

    /* Texts */
    val text: ColorResource,
    val textDisabled: ColorResource,
    val textWarning: ColorResource,

    /* Surfaces */
    val surfacePrimary: ColorResource,
    val onSurfacePrimary: ColorResource,
    val surfaceSecondary: ColorResource,
    val onSurfaceSecondary: ColorResource,
    val surfaceDisabled: ColorResource,
    val onSurfaceDisabled: ColorResource,

    ) {
    internal companion object {
        fun getColors() = Colors(
            accent = ColorResource.Purple500,
            onAccent = ColorResource.Black800,
            shadow = ColorResource.Black800,
            textDisabled = ColorResource.Black600,
            textWarning = ColorResource.CherryPop700,
            surfacePrimary = ColorResource.White900,
            surfaceDisabled = ColorResource.Black600,
            onSurfacePrimary = ColorResource.Black900,
            surfaceSecondary = ColorResource.Black700,
            onSurfaceSecondary = ColorResource.White900,
            onSurfaceDisabled = ColorResource.Black700,
            background = ColorResource.Black900,
            onBackground = ColorResource.White900,
            border = ColorResource.White900,
            borderDisabled = ColorResource.Black600,
            text = ColorResource.White900,
            backgroundOverlay = ColorResource.Black900_A_70,
        )
    }
}

@Preview(widthDp = 400, heightDp = 1500, showBackground = false)
@Composable
private fun PreviewColorSwatch() {
    Preview(showBackground = false) {
        val colors = Colors.getColors().let {
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
            items(colors) {(color,name) ->
                ColorCard(
                    color,
                    title = name,
                    description = ""
                )
            }
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

