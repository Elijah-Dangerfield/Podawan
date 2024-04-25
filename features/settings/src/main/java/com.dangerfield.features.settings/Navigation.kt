package com.dangerfield.features.settings

import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

fun Router.navigateToSettings() {
    navigate(settingsNavigationRoute.fill())
}

val settingsNavigationRoute = route("settings")

