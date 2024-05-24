package com.dangerfield.features.playlist

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToPlaylist() {
    navigate(
        playlistRoute.fill {
            isTopLevel(false)
            navAnimType(NavAnimType.SlideIn)
        }
    )
}

val playlistRoute = route("playlist") {
    // argument(someArgument)
}