package com.dangerfield.features.playback

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.openPlayer(id: String) {
    navigate(
        playerRoute.fill {
            arg(episodeIdArgument, id)
        }
    )
}

val playerRoute = route("player") {
    argument(episodeIdArgument)
}
