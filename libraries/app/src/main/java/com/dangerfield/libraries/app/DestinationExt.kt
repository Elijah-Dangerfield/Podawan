package com.dangerfield.libraries.app

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.dangerfield.libraries.app.ui.homeGraphRoute
import com.dangerfield.libraries.app.ui.libraryGraphRoute
import com.dangerfield.libraries.app.ui.searchGraphRoute
import com.dangerfield.libraries.navigation.Route

fun NavDestination.bottomTabRoute(hasSearchTab: Boolean): Route.Template? {
    return listOfNotNull(
        homeGraphRoute,
        searchGraphRoute.takeIf { hasSearchTab },
        libraryGraphRoute
    ).find { tabRoute ->
        this.hierarchy.any { it.route == tabRoute.navRoute }
    }
}

fun Route.Template?.toBottomTabName() =
    when (this) {
        homeGraphRoute -> "Home"
        searchGraphRoute -> "Search"
        libraryGraphRoute -> "Library"
        else -> null
    }
