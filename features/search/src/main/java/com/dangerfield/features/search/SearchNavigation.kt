package com.dangerfield.features.search

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToSearchA() {
    navigate(searchA.fill())
}

fun Router.navigateToSearchB() {
    navigate(searchB.fill())
}

fun Router.navigateToSearchC() {
    navigate(searchC.fill())
}

val searchRoute = route("search")

val searchA = route("searchA")

val searchB = route("searchB")

val searchC = route("searchC")