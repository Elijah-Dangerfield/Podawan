package com.dangerfield.podawan.apps.theBenAndEmilShow.config

import android.content.Context
import android.graphics.drawable.Drawable
import com.dangerfield.podawan.apps.theBenAndEmailShow.config.R
import podawan.core.AppConfiguration

class TheBenAndEmilShowAppConfiguration(
    private val context: Context
): AppConfiguration {
    override val appName: String = context.getString(R.string.ben_and_emil_show)
    override val rssFeedLink: String = "https://feeds.megaphone.fm/QCD4626915194"
    override val appIcon: Drawable = context.getDrawable(R.drawable.ben_and_emil_show)!!
}