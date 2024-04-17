package com.dangerfield.libraries.podcast

import podawan.core.Catching

interface PodcastRepository {
    suspend fun getPodcast(): Catching<PodcastShow>
}