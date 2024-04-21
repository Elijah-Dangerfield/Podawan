package com.dangerfield.libraries.app

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.dangerfield.libraries.navigation.Route

fun NavDestination.bottomTabRoute(): Route.Template? {
    return listOf(
        homeGraphRoute,
        searchGraphRoute,
        libraryGraphRoute
    ).find { tabRoute ->
        this.hierarchy.any { it.route == tabRoute.navRoute }
    }
}