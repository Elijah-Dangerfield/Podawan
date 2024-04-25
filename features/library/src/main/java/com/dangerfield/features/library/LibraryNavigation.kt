package com.dangerfield.features.library

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToLibrary() {
    navigate(
        libraryRoute.fill {
            //fill(someArgument, someValue)
        }
    )
}

val libraryRoute = route("library") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}