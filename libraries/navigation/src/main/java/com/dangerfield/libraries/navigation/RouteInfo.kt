package com.dangerfield.libraries.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import timber.log.Timber

fun NavBackStackEntry.asRouteInfo(): RouteInfo {

    val isStartDestination = destination.route?.substringBefore("?") == destination.parent?.startDestinationRoute?.substringBefore("?")
    val isOnlyUnfilledArgs = this.arguments?.isEmpty == true

    if (isStartDestination || isOnlyUnfilledArgs) {
        if (isOnlyUnfilledArgs) {
            Timber.d("Elijah, Route has only unfilled args: ${this.destination.route}")
        }

        if (isStartDestination) {
            Timber.d("Elijah, Detected start destination, using defaults. Route: ${this.destination.route}")
        }

        return RouteInfo(
            name = destination.route?.substringBefore("?") ?: "ROUTE DNE",
            navAnimType = NavAnimType.None,
            // if it lives in the main graph (not global destination) then assume its not top level
            isTopLevel = !destination.hierarchy.any { it.route == mainGraphRoute.navRoute }
        )
    }

    val navAnimType = this.arguments?.navArgument(navAnimTypeArg) ?: run {
        Timber.d("Route has no anim type arg, using default. Route name: ${this.destination.route}")
        navAnimTypeArg.argument.defaultValue as? NavAnimType ?: navAnimTypeDefault
    }

    val isTopLevel = this.arguments?.navArgument(isTopLevelArg) ?: run {
        Timber.d("Route has np isTopLevel type arg, using default. Route name: ${this.destination.route}")
        isTopLevelArg.argument.defaultValue as? Boolean ?: isTopLevelDefault
    }

    return RouteInfo(
        name = this.destination.route?.substringBefore("?") ?: "ROUTE DNE",
        navAnimType = navAnimType,
        isTopLevel = isTopLevel
    )
}

fun Route.Filled.asRouteInfo(): RouteInfo {
    return RouteInfo(
        name = route.substringBefore("?"),
        navAnimType = navAnimType,
        isTopLevel = isTopLevel
    )
}

data class RouteInfo(
    val name: String,
    val navAnimType: NavAnimType,
    val isTopLevel: Boolean
)