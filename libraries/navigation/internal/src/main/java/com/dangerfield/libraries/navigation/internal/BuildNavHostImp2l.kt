package com.dangerfield.libraries.navigation.internal

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.navigation.BuildNavHost
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@Stable
class BuildNavHostImpl2 @Inject constructor(
    private val navGraphRegistry: NavGraphRegistry,
    private val delegatingRouter: DelegatingRouter,
) : BuildNavHost {
    @Composable
    override fun invoke(startingRoute: Route.Filled) {

        val navController = rememberNavController(FloatingWindowNavigator())
        val coroutineScope = rememberCoroutineScope()
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        val actualRouter = remember {
            NavControllerRouter(
                navHostController = navController,
                coroutineScope = coroutineScope
            )
        }

        val currentFilledRoute by delegatingRouter.currentRouteFlow.collectAsStateWithLifecycle(null)
        val navBackStackEntry by actualRouter.navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val shouldHideBottomBar = currentFilledRoute?.isTopLevel == true

        Screen(
            bottomBar = {
                if (!shouldHideBottomBar) {
                    BottomBar(
                        items = listOf(
                            BottomBarItem(
                                title = "Home",
                                route = feedRoute.navRoute,
                                isSelected = currentDestination?.hierarchy?.any { it.route == feedRoute.navRoute } == true,
                                selectedIcon = PodawanIcon.HomeFilled("Home"),
                                unselectedIcon = PodawanIcon.HomeOutline("Home")
                            ),
                            BottomBarItem(
                                title = "Search",
                                route = searchRoute.navRoute,
                                selectedIcon = PodawanIcon.SearchFilled("Search"),
                                unselectedIcon = PodawanIcon.SearchOutline("Search"),
                                badgeAmount = 2,
                                isSelected = currentDestination?.hierarchy?.any { it.route == searchRoute.navRoute } == true
                            ),
                            BottomBarItem(
                                title = "Library",
                                route = libraryRoute.navRoute,
                                selectedIcon = PodawanIcon.LibraryFilled("Library"),
                                unselectedIcon = PodawanIcon.LibraryOutline("Library"),
                                isSelected = currentDestination?.hierarchy?.any { it.route == libraryRoute.navRoute } == true
                            )
                        ),
                        onItemClick = { item ->
                            actualRouter.navHostController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(actualRouter.navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) {
            NavHost(
                modifier = Modifier.padding(it),
                navController = actualRouter.navHostController,
                startDestination = startingRoute.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {
                navGraphRegistry.addRegistrations(this)

                delegatingRouter.setRouter(
                    router = actualRouter,
                    lifecycle = lifecycle,
                    startingRoute = startingRoute
                )
            }
        }
    }
}












