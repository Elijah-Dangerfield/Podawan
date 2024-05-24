package com.dangerfield.podawan.apps.theBenAndEmilShow.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.Colors
import com.dangerfield.libraries.ui.color.PreviewColorSwatch

object BenAndEmilShowColors : Colors {
    override val accent = ColorResource.Purple500
    override val onAccent = ColorResource.White900
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

@Preview(widthDp = 400, heightDp = 1500, showBackground = false)
@Composable
private fun PreviewBenAndEmilShowColors() {
    PreviewColorSwatch(colors = BenAndEmilShowColors)
}