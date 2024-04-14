package com.dangerfield.features.library

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

fun Router.navigateToLibrary() {
    navigate(
        fillRoute(libraryRoute) {
            //fill(someArgument, someValue)
        }
    )
}

val libraryRoute = route("library") {
    // argument(someArgument)
}