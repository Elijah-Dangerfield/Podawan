package com.dangerfield.podawan.apps.stuffYouShouldKnow.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.PreviewColorSwatch

object StuffYouShouldKnowColors : com.dangerfield.libraries.ui.color.Colors {
    override val accent = ColorResource.CherryPop700
    override val onAccent = ColorResource.White900
    override val shadow = ColorResource.Black900
    override val textDisabled = ColorResource.Black600
    override val textWarning = ColorResource.CherryPop700
    override val surfacePrimary = ColorResource.CherryPop700
    override val surfaceDisabled = ColorResource.Black400
    override val onSurfacePrimary = ColorResource.White900
    override val surfaceSecondary = ColorResource.Black700
    override val onSurfaceSecondary = ColorResource.Black500
    override val onSurfaceDisabled = ColorResource.Black600
    override val background = ColorResource.White900
    override val onBackground = ColorResource.Black900
    override val border = ColorResource.Black900
    override val borderDisabled = ColorResource.Black600
    override val text = ColorResource.Black900
    override val backgroundOverlay = ColorResource.Black900_A_30
    override val textSecondary = ColorResource.Black700
}

@Preview(widthDp = 400, heightDp = 1500, showBackground = false)
@Composable
private fun PreviewStuffYouShouldKnowColors() {
    PreviewColorSwatch(colors = StuffYouShouldKnowColors)
}