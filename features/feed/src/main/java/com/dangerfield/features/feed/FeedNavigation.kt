package com.dangerfield.features.feed

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.route

fun Router.navigateToFeedA() {
    navigate(feedA.noArgRoute())
}

fun Router.navigateToFeedB() {
    navigate(feedB.noArgRoute())
}

fun Router.navigateToFeedC() {
    navigate(feedC.noArgRoute())
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

val feedC = route("feedC") {
    // argument(someArgument)
}