package com.dangerfield.podawan.apps.stuffYouShouldKnow.config

import android.content.Context
import android.graphics.drawable.Drawable
import oddoneout.core.AppConfiguration

class StuffYouShouldKnowAppConfiguration(
    private val context: Context
): AppConfiguration {

    override val appIcon: Drawable = context.getDrawable(R.drawable.stuff_you_should_know)!!
    override val appName: String = context.getString(R.string.stuff_you_should_know)
    override val rssFeedLink: String = "https://omnycontent.com/d/playlist/e73c998e-6e60-432f-8610-ae210140c5b1/A91018A4-EA4F-4130-BF55-AE270180C327/44710ECC-10BB-48D1-93C7-AE270180C33E/podcast.rss"
}