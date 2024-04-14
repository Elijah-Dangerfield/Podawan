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

fun Router.navigateToSearchA() {
    navigate(searchA.noArgRoute())
}

fun Router.navigateToSearchB() {
    navigate(searchB.noArgRoute())
}

fun Router.navigateToSearchC() {
    navigate(searchC.noArgRoute())
}

val searchRoute = route("search") {
    // argument(someArgument)
}

val searchA = route("searchA") {
    // argument(someArgument)
}

val searchB = route("searchB") {
    // argument(someArgument)
}

val searchC = route("searchC") {
    // argument(someArgument)
}