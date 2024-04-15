package com.dangerfield.libraries.navigation.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.coreflowroutines.collectWithPreviousAsStateWithLifecycle
import com.dangerfield.libraries.navigation.BuildNavHost
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.noAnimEnter
import com.dangerfield.libraries.navigation.noAnimExit
import com.dangerfield.libraries.navigation.route
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Named

@AutoBind
@Stable
@Named("MultiGraphNavHost")
class MultiGraphNavHost @Inject constructor(
    private val navGraphRegistry: NavGraphRegistry,
    private val delegatingRouter: DelegatingRouter,
    private val determineNavAnimation: DetermineNavAnimation
) : BuildNavHost {
    @Composable
    override fun invoke(startingRoute: Route.Filled) {

        val navController = rememberNavController(FloatingWindowNavigator())
        val coroutineScope = rememberCoroutineScope()
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        val actualRouter = remember {
            NavControllerRouter(
                navHostController = navController,
                coroutineScope = coroutineScope,
                startingRoute = startingRoute
            )
        }

        val routeInfo by delegatingRouter.currentRouteFlow.collectWithPreviousAsStateWithLifecycle(
            startingRoute
        )
        val (prevRoute, currentRoute) = routeInfo

        val backStackEntryInfo by actualRouter.navHostController.currentBackStackEntryFlow.collectWithPreviousAsStateWithLifecycle(
            initialValue = null
        )

        val (previousBackStackEntry,currentBackStackEntry ) = backStackEntryInfo

        val currentDestination = currentBackStackEntry?.destination
        val previousDestination = previousBackStackEntry?.destination

        val shouldHideBottomBar = currentRoute.isTopLevel == true

        Screen(
            bottomBar = {
                AnimatedVisibility(
                    visible = !shouldHideBottomBar,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    BottomBar(
                        items = listOf(
                            BottomBarItem(
                                title = "Home",
                                route = "homeGraph",
                                isSelected = currentDestination?.hierarchy?.any { it.route == "homeGraph" } == true,
                                selectedIcon = PodawanIcon.HomeFilled("Home"),
                                unselectedIcon = PodawanIcon.HomeOutline("Home")
                            ),
                            BottomBarItem(
                                title = "Search",
                                route = "searchGraph",
                                selectedIcon = PodawanIcon.SearchFilled("Search"),
                                unselectedIcon = PodawanIcon.SearchOutline("Search"),
                                badgeAmount = 2,
                                isSelected = currentDestination?.hierarchy?.any { it.route == "searchGraph" } == true
                            ),
                            BottomBarItem(
                                title = "Library",
                                route = "libraryGraph",
                                selectedIcon = PodawanIcon.LibraryFilled("Library"),
                                unselectedIcon = PodawanIcon.LibraryOutline("Library"),
                                isSelected = currentDestination?.hierarchy?.any { it.route == "libraryGraph" } == true
                            )
                        ),
                        onItemClick = { item ->
                            actualRouter.navigate(
                                fillRoute(route(item.route)) {

                                    popUpTo(
                                        id = actualRouter.startDestination().id,
                                        saveState = true
                                    )

                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            )
                        }
                    )
                }
            }
        ) {

            val currentDestinationTab = currentDestination?.hierarchy?.find {
                it.route in listOf("homeGraph", "searchGraph", "libraryGraph")
            }?.route

            val prevDestinationTab = previousDestination?.hierarchy?.find {
                it.route in listOf("homeGraph", "searchGraph", "libraryGraph")
            }?.route

            val isBottomBarNav = currentDestinationTab != null
                    && prevDestinationTab != null
                    && currentDestinationTab != prevDestinationTab

            val navAnim = remember (currentRoute, prevRoute) {
                determineNavAnimation(currentRoute, prevRoute)
            }

            NavHost(
                modifier = Modifier.padding(it),
                navController = actualRouter.navHostController,
                startDestination = startingRoute.route,
                enterTransition = if (isBottomBarNav) { {noAnimEnter()} } else navAnim.enter,
                exitTransition =  if (isBottomBarNav) { {noAnimExit()} } else navAnim.exit,
                popEnterTransition = if (isBottomBarNav) { {noAnimEnter()} } else navAnim.popEnter,
                popExitTransition = if (isBottomBarNav) { {noAnimExit()} } else navAnim.popExit
            ) {

                actualRouter.addRoutes(
                    listOf(
                        startingRoute,
                        feedRoute.noArgRoute(navAnimType = NavAnimType.None),
                        searchRoute.noArgRoute(navAnimType = NavAnimType.None),
                        libraryRoute.noArgRoute(navAnimType = NavAnimType.None)
                    )
                )

                navGraphRegistry.addTopLevelDestinations(this)

                navigation(
                    route = "mainGraph",
                    startDestination = "homeGraph",
                ) {
                    navigation(
                        route = "homeGraph",
                        startDestination = feedRoute.navRoute,
                    ) {
                        navGraphRegistry.addHomeDestinations(this)
                    }

                    navigation(
                        route = "searchGraph",
                        startDestination = searchRoute.navRoute
                    ) {
                        // add all search destinations
                        navGraphRegistry.addSearchDestinations(this)
                    }

                    navigation(
                        route = "libraryGraph",
                        startDestination = libraryRoute.navRoute
                    ) {
                        // add all library destinations
                        navGraphRegistry.addLibraryDestinations(this)
                    }
                }

                delegatingRouter.setRouter(
                    router = actualRouter,
                    lifecycle = lifecycle,
                    startingRoute = startingRoute
                )
            }
        }
    }
}

