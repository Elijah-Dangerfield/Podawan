package com.dangerfield.features.feed.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.feed.feedRoute
import se.ansman.dagger.auto.AutoBindIntoSet
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): HomeTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = feedRoute.navRoute,
            arguments = feedRoute.navArguments
        ) {
            FeedScreen()
        }
    }
}