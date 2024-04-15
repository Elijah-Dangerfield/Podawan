package com.dangerfield.libraries.navigation

import androidx.navigation.NavGraphBuilder


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