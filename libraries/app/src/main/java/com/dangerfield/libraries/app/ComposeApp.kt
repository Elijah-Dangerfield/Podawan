package com.dangerfield.libraries.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.inAppMessaging.UpdateStatus
import com.dangerfield.features.inAppMessaging.internal.update.DownloadProgressBar
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.coreflowroutines.collectWithPrevious
import com.dangerfield.libraries.coreflowroutines.observeWithLifecycle
import com.dangerfield.libraries.navigation.DelegatingRouter
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.internal.NavControllerRouter
import com.dangerfield.libraries.navigation.NavGraphRegistry
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.RouteInfo
import com.dangerfield.libraries.navigation.asRouteInfo
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowHost
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.mainGraphRoute
import com.dangerfield.libraries.network.internal.OfflineBar
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.PodawanSnackbarVisuals
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.Snackbar
import com.dangerfield.ui.components.podawanSnackbarData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import podawan.core.SnackBarPresenter
import podawan.core.allOrNone
import timber.log.Timber

@Composable
fun PodawanApp(
    startingRouteTemplate: Route.Template,
    delegatingRouter: DelegatingRouter,
    updateStatus: UpdateStatus?,
    navGraphRegistry: NavGraphRegistry
) {

    val isOffline by LocalAppState.current.isOffline.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val floatingWindowNavigator = remember { FloatingWindowNavigator() }
    val navController = rememberNavController(floatingWindowNavigator)
    val coroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val filledStartingRoute = rememberSaveable(startingRouteTemplate) {
        startingRouteTemplate.fill {
            isTopLevel = startingRouteTemplate != mainGraphRoute
            navAnimType = NavAnimType.None
        }
    }

    val actualRouter = remember {
        NavControllerRouter(
            navHostController = navController,
            coroutineScope = coroutineScope,
        )
    }

    var currentRouteInfo: RouteInfo by remember { mutableStateOf(filledStartingRoute.asRouteInfo()) }
    var prevRouteInfo: RouteInfo? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        delegatingRouter
            .currentRouteInfo
            .collectWithPrevious { prev, curr ->
                currentRouteInfo = curr
                prevRouteInfo = prev
            }
    }

    var previousDestination: NavDestination? by remember { mutableStateOf(null) }
    var currentDestination: NavDestination? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        navController
            .currentBackStackEntryFlow
            .map { it.destination }
            .distinctUntilChanged()
            .collectWithPrevious { prev, curr ->
                currentDestination = curr
                previousDestination = prev
            }
    }

    val isSwitchingBottomTabs = allOrNone(
        currentDestination?.bottomTabRoute(),
        previousDestination?.bottomTabRoute()
    ) { currentTab, previousTab ->
        currentTab != previousTab
    } ?: false

    LaunchedEffect(currentRouteInfo, prevRouteInfo, currentDestination, previousDestination) {
        Timber.d(
            """
            ------------------------------------------------------
            currentRouteInfo: $currentRouteInfo
            prevRouteInfo: $prevRouteInfo
            
            isSwitchingBottomTabs: $isSwitchingBottomTabs
            ------------------------------------------------------
        """.trimIndent()
        )

    }

    val currentSelectedTabRoute = currentDestination?.bottomTabRoute() ?: homeGraphRoute

    val navAnim = determineNavAnimation(
        to = currentRouteInfo,
        from = prevRouteInfo,
        currentDestination = currentDestination,
        previousDestination = previousDestination
    )

    LaunchedEffect(Unit) {
        SnackBarPresenter
            .messagesFlow
            .observeWithLifecycle(lifecycleOwner.lifecycle) {
                val result = snackbarHostState.showSnackbar(
                    PodawanSnackbarVisuals(
                        message = it.message,
                        isDebug = it.isDebug,
                        withDismissAction = !it.autoDismiss,
                        duration = if (it.autoDismiss) SnackbarDuration.Short else SnackbarDuration.Indefinite,
                        title = it.title,
                        actionLabel = it.actionLabel
                    )
                )

                if (result == SnackbarResult.ActionPerformed) {
                    it.action?.invoke()
                }
            }
    }

    LaunchedEffect(
        actualRouter,
        lifecycle,
        startingRouteTemplate,
    ) {
        delegatingRouter.setRouter(
            router = actualRouter,
            lifecycle = lifecycle,
        )

        // Starting destinations arent shown in response to a call with a filled route,
        // so we preemptively register the filled routes here so that when they are
        // added to the backstack we can expose the correct RouteInfo
        actualRouter.registerStartingRoutes(
            startingRouteTemplate to filledStartingRoute,

            homeGraphRoute to homeGraphRoute.fill(),
            feedRoute to feedRoute.fill(),

            searchGraphRoute to searchGraphRoute.fill(),
            searchRoute to searchRoute.fill(),

            libraryRoute to libraryRoute.fill(),
            libraryGraphRoute to libraryGraphRoute.fill(),
        )
    }


    PodawanTheme {
        Screen(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { snackbarData ->
                        (snackbarData.visuals as? PodawanSnackbarVisuals)?.let {
                            val data = remember {
                                podawanSnackbarData(
                                    visuals = it,
                                    onAction = snackbarData::performAction,
                                    onDismiss = snackbarData::dismiss
                                )
                            }
                            Snackbar(
                                podawanSnackbarData = data
                            )
                        }
                    }
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = !currentRouteInfo.isTopLevel,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    AppBottomBar(
                        currentTabRoute = currentSelectedTabRoute,
                        onItemClick = { item ->
                            actualRouter.navigate(
                                item.fill {
                                    navAnimType = NavAnimType.None

                                    launchSingleTop = true
                                    restoreState = true

                                    popUpTo(
                                        actualRouter.navHostController.graph.findStartDestination().id,
                                        saveState = true
                                    )
                                }
                            )
                        }
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {

                OfflineBar(isOffline)

                DownloadProgressBar(updateStatus)

                NavHost(
                    //modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = startingRouteTemplate.navRoute,
                    enterTransition = navAnim.enter,
                    exitTransition = navAnim.exit,
                    popEnterTransition = navAnim.popEnter,
                    popExitTransition = navAnim.popExit
                ) {

                    navGraphRegistry.addGlobalDestinations(this)

                    navigation(
                        route = mainGraphRoute.navRoute,
                        arguments = mainGraphRoute.navArguments,
                        startDestination = homeGraphRoute.navRoute,
                    ) {
                        navigation(
                            route = homeGraphRoute.navRoute,
                            arguments = homeGraphRoute.navArguments,
                            startDestination = feedRoute.navRoute,
                        ) {
                            navGraphRegistry.addHomeDestinations(this)
                        }

                        navigation(
                            route = searchGraphRoute.navRoute,
                            arguments = searchGraphRoute.navArguments,
                            startDestination = searchRoute.navRoute
                        ) {
                            navGraphRegistry.addSearchDestinations(this)
                        }

                        navigation(
                            route = libraryGraphRoute.navRoute,
                            arguments = libraryGraphRoute.navArguments,
                            startDestination = libraryRoute.navRoute
                        ) {
                            navGraphRegistry.addLibraryDestinations(this)
                        }
                    }
                }

                FloatingWindowHost(floatingWindowNavigator)
            }
        }
    }
}
