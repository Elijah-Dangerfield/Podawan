package com.dangerfield.podawan.apps.fragmented.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.PreviewColorSwatch

object FragmentedColors : com.dangerfield.libraries.ui.color.Colors {
    override val accent = ColorResource.MintyFresh300
    override val onAccent = ColorResource.Black900
    override val shadow = ColorResource.Black900
    override val textDisabled = ColorResource.Black600
    override val textWarning = ColorResource.CherryPop700
    override val surfacePrimary = ColorResource.Sky400
    override val surfaceDisabled = ColorResource.Black400
    override val onSurfacePrimary = ColorResource.White900
    override val surfaceSecondary = ColorResource.Black100
    override val onSurfaceSecondary = ColorResource.Black700
    override val onSurfaceDisabled = ColorResource.Black600
    override val background = ColorResource.Sky100
    override val onBackground = ColorResource.Black900
    override val border = ColorResource.Black900
    override val borderDisabled = ColorResource.Black600
    override val text = ColorResource.Black900
    override val backgroundOverlay = ColorResource.Black900_A_30
    override val textSecondary = ColorResource.Black700
}

@Preview(widthDp = 400, heightDp = 1500, showBackground = false)
@Composable
private fun PreviewTmgColors() {
    PreviewColorSwatch(colors = FragmentedColors)
}
