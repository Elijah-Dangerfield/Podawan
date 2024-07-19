package com.dangerfield.features.playlist.internal

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.toPlaylistEpisodeDetails(id: String) {
    navigate(
        playlistEpisodeDetailsRoute.fill {
            arg(episodeIdArgument, id)
        }
    )
}

val playlistEpisodeDetailsRoute = route("playlistEpisodeDetails") {
    argument(episodeIdArgument)
}