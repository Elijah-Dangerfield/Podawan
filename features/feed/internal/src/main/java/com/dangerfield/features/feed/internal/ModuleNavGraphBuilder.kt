package com.dangerfield.features.feed.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToBlockingError
import com.dangerfield.features.feed.feedA
import com.dangerfield.features.feed.feedB
import com.dangerfield.features.feed.feedC
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.feed.navigateToFeedA
import com.dangerfield.features.feed.navigateToFeedB
import com.dangerfield.features.feed.navigateToFeedC
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import se.ansman.dagger.auto.AutoBindIntoSet
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.button.Button
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : HomeTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = feedRoute.navRoute,
            arguments = feedRoute.navArguments
        ) {
            FeedScreen(
                onClick = {
                    router.navigateToBlockingError()
                }
            )
        }

        composable(
            route = feedA.navRoute,
            arguments = feedA.navArguments
        ) {
            Screen {
                Text("Feed A")
                Button(onClick = { router.navigateToFeedB() }) {
                    Text(text = "To Feed B")
                }
            }
        }

        composable(
            route = feedB.navRoute,
            arguments = feedB.navArguments
        ) {
            Screen {
                Text("Feed B")
                Button(onClick = { router.navigateToFeedC() }) {
                    Text(text = "Feed C")
                }
            }
        }

        composable(
            route = feedC.navRoute,
            arguments = feedC.navArguments
        ) {
            Screen(
                containerColor = PodawanTheme.colors.accent.color
            ) {
                Text("Feed C")
            }
        }
    }
}