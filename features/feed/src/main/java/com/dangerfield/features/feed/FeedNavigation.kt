package com.dangerfield.features.feed

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route


fun Router.toEpisodeDetails(id: String) {
    navigate(
        episodeDetailsRoute.fill {
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

val episodeIdArgument = navArgument("episodeId") {
    type = NavType.StringType
}

val episodeDetailsRoute = route("episodeDetails") {
     argument(episodeIdArgument)
}


val showDetailsRoute = route("showDetails") {
    isTopLevelAlways(true)
    animTypeAlways(NavAnimType.SlideUp)
}
