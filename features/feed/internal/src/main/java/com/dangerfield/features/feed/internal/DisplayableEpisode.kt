package com.dangerfield.features.feed.internal

data class DisplayableEpisode(
    val title: String,
    val releaseDate: String,
    val imageUrl: String,
    val description: String,
    val fallbackImageUrl: String,
    val isPlaying: Boolean,
    val isDownloaded: Boolean,
    val id: String,
)