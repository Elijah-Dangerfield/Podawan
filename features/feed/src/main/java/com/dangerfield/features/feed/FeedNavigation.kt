package com.dangerfield.features.feed

import com.dangerfield.features.login.loginRoute
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

fun Router.navigateToFeed() {
    navigate(
        fillRoute(feedRoute) {
            //fill(someArgument, someValue)
            popUpTo(loginRoute, inclusive = true)
        }
    )
}

val feedRoute = route("feed") {
    // argument(someArgument)
}