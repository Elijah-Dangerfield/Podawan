package com.dangerfield.features.search

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToSearch() {
    navigate(
        searchRoute.fill {
            //fill(someArgument, someValue)
        }
    )
}

fun Router.navigateToSearchA() {
    navigate(searchA.fill())
}

fun Router.navigateToSearchB() {
    navigate(searchB.fill())
}

fun Router.navigateToSearchC() {
    navigate(searchC.fill())
}

val searchRoute = route("search") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}

val searchA = route("searchA") {
    // argument(someArgument)
    isTopLevel(true)
}

val searchB = route("searchB") {
    // argument(someArgument)
}

val searchC = route("searchC") {
    // argument(someArgument)
}