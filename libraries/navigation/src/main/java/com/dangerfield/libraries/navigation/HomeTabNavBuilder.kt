package com.dangerfield.libraries.navigation

import androidx.navigation.NavGraphBuilder


interface HomeTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface SearchTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface LibraryTabNavBuilder {
    fun NavGraphBuilder.addDestinations(router: Router)
}

interface TopLevelNavBuilder {
    fun NavGraphBuilder.addTopLevelDestinations(router: Router)
}