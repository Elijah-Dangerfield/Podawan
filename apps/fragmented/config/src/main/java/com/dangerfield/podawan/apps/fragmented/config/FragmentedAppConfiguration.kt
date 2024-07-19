package com.dangerfield.podawan.apps.fragmented.config

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import dagger.hilt.android.qualifiers.ApplicationContext
import podawan.core.AppConfiguration

@Immutable

class FragmentedAppConfiguration(
    @ApplicationContext private val context: Context
): AppConfiguration {

    override val appIcon: Drawable = context.getDrawable(R.drawable.android)!!
    override val appName: String = context.getString(R.string.fragmented)
    override val rssFeedLink: String = "https://feeds.simplecast.com/LpAGSLnY"
}
