package com.dangerfield.features.library.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): HomeTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = libraryRoute.navRoute,
            arguments = libraryRoute.navArguments
        ) {
            // TODO - add your feature composable here
        }
    }
}