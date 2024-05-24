package com.dangerfield.features.playlist

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToNewPlaylist() {
    navigate(newPlaylistRoute.fill())
}

val newPlaylistRoute = route("newPlaylist")