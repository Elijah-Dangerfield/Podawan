package com.dangerfield.features.search.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.SearchTabNavBuilder
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): SearchTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = searchRoute.navRoute,
            arguments = searchRoute.navArguments
        ) {
            SearchScreen()
        }
    }
}