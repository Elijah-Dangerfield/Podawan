package com.dangerfield.libraries.app

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.dangerfield.libraries.navigation.Route

fun NavDestination.bottomTabRoute(): Route.Template? {
    return listOf(
        com.dangerfield.libraries.app.homeGraphRoute,
        com.dangerfield.libraries.app.searchGraphRoute,
        com.dangerfield.libraries.app.libraryGraphRoute
    ).find { tabRoute ->
        this.hierarchy.any { it.route == tabRoute.navRoute }
    }
}