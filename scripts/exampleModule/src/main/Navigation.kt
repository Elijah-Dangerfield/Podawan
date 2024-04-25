package com.dangerfield.features.example

import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToExample() {
    navigate(
        exampleRoute.fill {
            isTopLevel(false)
            navAnimType(NavAnimType.SlideIn)
        }
    )
}

val exampleRoute = route("example") {
    // argument(someArgument)
}