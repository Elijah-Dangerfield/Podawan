package com.dangerfield.podawan.apps.fragmented.config

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import dagger.hilt.android.qualifiers.ApplicationContext
import podawan.core.AppVariantConfiguration
import podawan.core.FontConfig

@Immutable

class FragmentedAppVariantConfiguration(
    @ApplicationContext private val context: Context
): AppVariantConfiguration {

    override val appIcon: Drawable = context.getDrawable(R.drawable.android)!!
    override val appName: String = context.getString(R.string.fragmented)
    override val rssFeedLink: String = "https://feeds.simplecast.com/LpAGSLnY"
    override val hasSearchTab: Boolean = false
    override val fontConfig: FontConfig = FontConfig.Custom(
        headerFamily = FontFamily.Monospace,
        bodyFamily = FontFamily.Monospace,
        labelFamily = FontFamily.Monospace,
        displayFamily = FontFamily.Monospace
    )
}
