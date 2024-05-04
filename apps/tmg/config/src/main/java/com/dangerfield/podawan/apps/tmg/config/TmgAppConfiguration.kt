package com.dangerfield.podawan.apps.tmg.config

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import podawan.core.AppConfiguration

class TmgAppConfiguration(
    @ApplicationContext private val context: Context
): AppConfiguration {

    override val appIcon: Drawable = context.getDrawable(R.drawable.tmg)!!
    override val appName: String = context.getString(R.string.tmg)
    override val rssFeedLink: String = "https://audioboom.com/channels/5024507.rss"
}
