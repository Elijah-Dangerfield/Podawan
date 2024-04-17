package com.dangerfield.libraries.podcast.internal

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PodcastInternalModule {

    @Provides
    @Singleton
    fun provideRssParser(): RssParser {
        return RssParserBuilder(
            callFactory = OkHttpClient(),
            charset = Charsets.UTF_8,
        ).build()
    }

}