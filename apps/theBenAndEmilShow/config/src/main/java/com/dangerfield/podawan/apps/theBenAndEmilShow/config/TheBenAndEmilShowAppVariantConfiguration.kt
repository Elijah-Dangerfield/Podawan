package com.dangerfield.podawan.apps.theBenAndEmilShow.config

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import com.dangerfield.podawan.apps.theBenAndEmailShow.config.R
import podawan.core.AppVariantConfiguration
import podawan.core.FontConfig

@Immutable

class TheBenAndEmilShowAppVariantConfiguration(
    private val context: Context
): AppVariantConfiguration {
    override val appName: String = context.getString(R.string.ben_and_emil_show)
    override val rssFeedLink: String = "https://feeds.megaphone.fm/QCD4626915194"
    override val appIcon: Drawable = context.getDrawable(R.drawable.ben_and_emil_show)!!
    override val hasSearchTab: Boolean = true
    override val fontConfig: FontConfig = FontConfig.Default
}