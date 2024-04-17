package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class DefaultPodcastRepository @Inject constructor(
    private val rssParser: RssParser,
) : PodcastRepository {

    override suspend fun getPodcast(): Catching<PodcastShow> = Catching {
        val rssChannel: RssChannel =
            rssParser.getRssChannel("https://feeds.megaphone.fm/QCD4626915194")

        rssChannel.toPodcastShow()
    }
}