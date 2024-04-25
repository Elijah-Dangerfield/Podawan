package com.dangerfield.libraries.podcast


data class PodcastShow(
    val title: String?,
    val link: String?,
    val description: String?,
    val image: HeroImage?,
    val lastBuildDate: String?,
    val updatePeriod: String?,
    val items: List<PodcastEpisode>,
    val itunesChannelData: ItunesChannelData?
)

data class HeroImage(
    val title: String?,
    val url: String?,
    val link: String?,
    val description: String?
)

data class PodcastEpisode(
    val guid: String?,
    val title: String?,
    val author: String?,
    val link: String?,
    val fallbackImageUrl: String?,
    val pubDate: String?,
    val description: String?,
    val content: String?,
    val image: String?,
    val audio: String?,
    val video: String?,
    val sourceName: String?,
    val sourceUrl: String?,
    val categories: List<String>,
    val itunesItemData: EpisodeItunesData?,
    val commentsUrl: String?,
)

data class EpisodeItunesData(
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

data class ItunesChannelData(
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