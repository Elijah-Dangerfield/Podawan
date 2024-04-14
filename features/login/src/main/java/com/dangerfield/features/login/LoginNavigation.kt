package com.dangerfield.features.login

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

fun Router.navigateToLogin() {
    navigate(
        fillRoute(loginRoute) {
            //fill(someArgument, someValue)
        }
    )
}

val loginRoute = route("login") {
    // argument(someArgument)
}