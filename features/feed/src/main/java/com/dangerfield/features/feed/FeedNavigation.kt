package com.dangerfield.features.feed

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

fun Router.navigateToFeedA() {
    navigate(
        feedA.noArgRoute()
    )
}

fun Router.navigateToFeedB() {
    navigate(feedB.noArgRoute())
}

fun Router.navigateToFeedC() {
    navigate(
        fillRoute(feedC) {
            isTopLevel = true
            navAnimType = NavAnimType.SlideOver
        }
    )
}

val feedRoute = route("feed") {
    // argument(someArgument)
}

val feedA = route("feedA") {
    // argument(someArgument)
}

val feedB = route("feedB") {
    // argument(someArgument)
}

val feedC = route("feedC", isTopLevel = true) {
    // argument(someArgument)
    isTopLevel(true)
}