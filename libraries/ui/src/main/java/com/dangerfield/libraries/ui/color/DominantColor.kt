package com.dangerfield.libraries.ui.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.dangerfield.libraries.ui.LocalColors
import com.kmpalette.DominantColorState
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
fun rememberDominantColorState(
    defaultColor: ColorResource = LocalColors.current.surfaceSecondary,
    defaultOnColor: ColorResource = LocalColors.current.onSurfaceSecondary,
    cacheSize: Int = 0,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isSwatchValid: (Palette.Swatch) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<ImageBitmap> = com.kmpalette.rememberDominantColorState(
    defaultColor = defaultColor.color,
    defaultOnColor = defaultOnColor.color,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isSwatchValid = isSwatchValid,
    builder = builder,
)

