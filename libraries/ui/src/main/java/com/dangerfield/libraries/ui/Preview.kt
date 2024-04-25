package com.dangerfield.libraries.ui

import android.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.dictionary.Dictionary
import com.dangerfield.libraries.dictionary.LocalDictionary
import com.dangerfield.libraries.dictionary.applyArgs
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.Colors
import com.dangerfield.libraries.ui.theme.PodawanTheme
import podawan.core.Catching
import podawan.ui.R


@Composable
internal fun Preview(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    showBackground: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {

    var context = LocalContext.current
    if (context !is ContextThemeWrapper && context !is ContextThemeWrapper) {
        context = ContextThemeWrapper(
            LocalContext.current,
            R.style.Theme_Spyfall
        )
    }

    val previewDictionary = object : Dictionary {
        override fun getString(key: Int, args: Map<String, String>): String = Catching {
            context.getText(key).toString().applyArgs(args)
        }.getOrNull() ?: "DNE"

        override fun getOptionalString(key: Int, args: Map<String, String>): String? = Catching {
            context.getText(key).toString().applyArgs(args)
        }.getOrNull()
    }

    CompositionLocalProvider(
        LocalColors provides PreviewColors,
        LocalContext provides context,
        LocalDictionary provides previewDictionary,
    ) {
        PodawanTheme{
            Box(
                modifier = modifier
                    .thenIf(showBackground) {
                        background(PodawanTheme.colors.background.color)
                    }
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}

object PreviewColors : Colors {
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