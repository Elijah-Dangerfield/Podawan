package com.dangerfield.features.playback.internal.ui

import com.dangerfield.features.playback.episodeIdArgument
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.openEpisodeOptions(id: String) {
    navigate(
        episodeOptions.fill {
            arg(episodeIdArgument, id)
        }
    )
}

val episodeOptions = route("episodeOptions") {
    argument(episodeIdArgument)
}
