package com.dangerfield.features.feed

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route


fun Router.toFeedEpisodeDetails(id: String) {
    navigate(
        feedEpisodeDetails.fill {
            arg(episodeIdArgument, id)
        }
    )
}

fun Router.toShowDetails() {
    navigate(
        showDetailsRoute.fill(),

    )
}

val feedRoute = route("feed") {
    isTopLevelAlways(false)
    animTypeAlways(NavAnimType.None)
}



val feedEpisodeDetails = route("episodeDetails") {
     argument(episodeIdArgument)
}


val showDetailsRoute = route("showDetails") {
    isTopLevelAlways(true)
    animTypeAlways(NavAnimType.SlideUp)
}
