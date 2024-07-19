package com.dangerfield.features.playlist

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route


val playlistIdArgument = navArgument("playlistId") {
    type = NavType.IntType
}

val newPlaylistRoute = route("newPlaylist")

val addToPlaylistRoute = route("addToPlaylist") {
    argument(episodeIdArgument)
}

val playlistRoute = route("playlist") {
    argument(playlistIdArgument)
}

val editPlaylistRoute = route("editPlaylistRoute") {
    argument(playlistIdArgument)
    isTopLevelAlways(true)
    animTypeAlways(NavAnimType.SlideUp)
}

fun Router.navigateToNewPlaylist() {
    navigate(newPlaylistRoute.fill())
}

fun Router.navigateToAddToPlaylist(
    id: String,
) {
    navigate(addToPlaylistRoute.fill {
        arg(episodeIdArgument, id)
    })
}


fun Router.navigateToPlaylist(id: Int, popOff: Route.Template? = null) {
    navigate(
        playlistRoute.fill {
            arg(playlistIdArgument, id)
            popOff?.let {
                popBackTo(popOff, inclusive = true)
            }
        }
    )
}

fun Router.navigateToEditPlaylist(id: Int, popOff: Route.Template? = null) {
    navigate(
        editPlaylistRoute.fill {
            arg(playlistIdArgument, id)
            popOff?.let {
                popBackTo(popOff, inclusive = true)
            }
        }
    )
}

