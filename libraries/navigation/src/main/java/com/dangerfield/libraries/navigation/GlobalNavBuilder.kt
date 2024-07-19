package com.dangerfield.libraries.navigation

import androidx.navigation.NavGraphBuilder


/**
 * Global nav builder should be used for destinations with context app wide.
 * This usually means the bottom bar will be hidden for these destinations but
 * not always.
 */
interface GlobalNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface SearchTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface LibraryTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface HomeTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}