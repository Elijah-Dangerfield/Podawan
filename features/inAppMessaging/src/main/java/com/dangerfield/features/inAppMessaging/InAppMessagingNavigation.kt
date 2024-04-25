package com.dangerfield.features.inAppMessaging

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToInAppMessaging() {
    navigate(
        inAppMessagingRoute.fill()
    )
}

val inAppMessagingRoute = route("inAppMessaging") {
    // argument(someArgument)
}