package com.dangerfield.libraries.podcast

import kotlin.time.Duration


data class PodcastShow(
    val rssFeedLink: String,
    val title: String?,
    val link: String?,
    val description: String?,
    val heroImage: HeroImage?,
    val lastBuildDate: String?,
    val updatePeriod: String?,
    val episodes: List<Episode>,
    val itunesShowData: ItunesShowData?
)

data class HeroImage(
    val title: String?,
    val url: String?,
    val link: String?,
    val description: String?
)

data class Episode(
    val guid: String,
    val title: String?,
    val author: String?,
    val link: String?,
    val pubDate: String?,
    val description: String?,
    val content: String?,
    val images: List<String>,
    val audio: String?,
    val video: String?,
    val sourceName: String?,
    val sourceUrl: String?,
    val showRssFeedLink: String,
    val categories: List<String>,
    val itunesItemData: ItunesEpisodeData?,
    val commentsUrl: String?,
    val resumePoint: Duration,
    val totalDuration: Duration?
)

data class ItunesEpisodeData(
    val author: String?,
    val duration: String?,
    val episode: String?,
    val episodeType: String?,
    val explicit: String?,
    val image: String?,
    val keywords: List<String>,
    val subtitle: String?,
    val summary: String?,
    val season: String?,
)

data class ItunesShowData(
    val author: String?,
    val categories: List<String> = emptyList(),
    val duration: String?,
    val explicit: String?,
    val image: String?,
    val keywords: List<String>,
    val newsFeedUrl: String?,
    val owner: ItunesOwner?,
    val subtitle: String?,
    val summary: String?,
    val type: String?,
)

data class ItunesOwner(
    val name: String?,
    val email: String?,
)