package com.dangerfield.features.search

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

fun Router.navigateToSearch() {
    navigate(
        fillRoute(searchRoute) {
            //fill(someArgument, someValue)
        }
    )
}

val searchRoute = route("search") {
    // argument(someArgument)
}