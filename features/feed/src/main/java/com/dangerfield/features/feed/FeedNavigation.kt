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

val feedRoute = route("feed") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}

val episodeIdArgument = navArgument("episodeId") {
    type = NavType.StringType
}

val episodeDetailsRoute = route("episodeDetails") {
     argument(episodeIdArgument)
}
