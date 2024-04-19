package com.dangerfield.libraries.navigation.internal

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowHost
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.getFloatingWindowNavigator
import com.dangerfield.libraries.navigation.mainGraphRoute
import com.dangerfield.ui.components.Screen
import kotlinx.coroutines.flow.map
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
@Stable
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

        val (prevRoute, currentRoute) = delegatingRouter
            .currentRouteFlow
            .collectWithPreviousAsStateWithLifecycle(startingRoute)
            .value

        val (previousDestination, currentDestination) = actualRouter
            .navHostController
            .currentBackStackEntryFlow
            .map { it.destination }
            .collectWithPreviousAsStateWithLifecycle(initialValue = null)
            .value

        val shouldHideBottomBar = currentRoute.isTopLevel == true
        val currentSelectedTabRoute = currentDestination?.bottomTabRoute() ?: homeGraphRoute

        LaunchedEffect(currentRoute, currentDestination) {
            Log.d("Elijah", """
                ------------------------------------------------------------------
                currentRoute: ${currentRoute.route}
                isTopLevel: ${currentRoute.isTopLevel}
                currentDestination: ${currentDestination?.route}
                ------------------------------------------------------------------
            """.trimIndent())
        }

        Screen(
            bottomBar = {
                AnimatedVisibility(
                    visible = !shouldHideBottomBar,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    AppBottomBar(
                        currentTabRoute = currentSelectedTabRoute,
                        onItemClick = { item ->
                            actualRouter.navigate(
                                fillRoute(item) {
                                    popUpTo(
                                        id = actualRouter.startDestination().id,
                                        saveState = true
                                    )
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            )
                        }
                    )
                }
            }
        ) {

            val navAnim = remember(currentRoute, prevRoute, currentDestination, previousDestination) {
                determineNavAnimation(
                    to = currentRoute,
                    from = prevRoute,
                    currentDestination = currentDestination,
                    previousDestination = previousDestination
                )
            }

            NavHost(
                //modifier = Modifier.padding(it),
                navController = actualRouter.navHostController,
                startDestination = startingRoute.route,
                enterTransition = navAnim.enter,
                exitTransition = navAnim.exit,
                popEnterTransition = navAnim.popEnter,
                popExitTransition = navAnim.popExit
            ) {

                // the base screens of a tab are never navigated to, so they are never added to the
                // list inside the router that allows us to track the current route
                // which we use for determining the nav animation. So we need to manually add them
                actualRouter.addRoutes(
                    listOf(
                        startingRoute,
                        feedRoute.noArgRoute(navAnimType = NavAnimType.None),
                        searchRoute.noArgRoute(navAnimType = NavAnimType.None),
                        libraryRoute.noArgRoute(navAnimType = NavAnimType.None)
                    )
                )

                navGraphRegistry.addGlobalDestinations(this)

                navigation(
                    route = mainGraphRoute.navRoute,
                    startDestination = homeGraphRoute.navRoute,
                ) {
                    navigation(
                        route = homeGraphRoute.navRoute,
                        startDestination = feedRoute.navRoute,
                    ) {
                        navGraphRegistry.addHomeDestinations(this)
                    }

                    navigation(
                        route = searchGraphRoute.navRoute,
                        startDestination = searchRoute.navRoute
                    ) {
                        navGraphRegistry.addSearchDestinations(this)
                    }

                    navigation(
                        route = libraryGraphRoute.navRoute,
                        startDestination = libraryRoute.navRoute
                    ) {
                        navGraphRegistry.addLibraryDestinations(this)
                    }
                }


                delegatingRouter.setRouter(
                    router = actualRouter,
                    lifecycle = lifecycle,
                    startingRoute = startingRoute
                )
            }

            val floatingWindowNavigator = remember {
                actualRouter.navHostController.getFloatingWindowNavigator()
            }

            floatingWindowNavigator?.let {
                FloatingWindowHost(floatingWindowNavigator)
            }
        }
    }
}

