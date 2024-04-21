package com.dangerfield.libraries.app

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.coreflowroutines.collectWithPreviousAsStateWithLifecycle
import com.dangerfield.libraries.coreflowroutines.observeWithLifecycle
import com.dangerfield.libraries.navigation.DelegatingRouter
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.NavControllerRouter
import com.dangerfield.libraries.navigation.NavControllerRouterSaver
import com.dangerfield.libraries.navigation.NavGraphRegistry
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowHost
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.getFloatingWindowNavigator
import com.dangerfield.libraries.navigation.internal.determineNavAnimation
import com.dangerfield.libraries.navigation.mainGraphRoute
import com.dangerfield.libraries.network.internal.OfflineBar
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.PodawanSnackbarVisuals
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.Snackbar
import com.dangerfield.ui.components.podawanSnackbarData
import kotlinx.coroutines.flow.map
import podawan.core.SnackBarPresenter
import timber.log.Timber

val floatingWindowNavigator = FloatingWindowNavigator()
@Composable
fun PodawanApp(
    startingRoute: Route.Filled,
    navGraphRegistry: NavGraphRegistry
) {
    val isOffline by LocalAppState.current.isOffline.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController(floatingWindowNavigator)
    val coroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

//    val actualRouter = remember {
//        NavControllerRouter(
//            navHostController = navController,
//            coroutineScope = coroutineScope,
//            startingRoute = startingRoute
//        )
//    }

//    val (prevRoute, currentRoute) = delegatingRouter
//        .currentRouteFlow
//        .collectWithPreviousAsStateWithLifecycle(startingRoute)
//        .value
//
//    val (previousDestination, currentDestination) = navController
//        .currentBackStackEntryFlow
//        .map { it.destination }
//        .collectWithPreviousAsStateWithLifecycle(initialValue = null)
//        .value

    //val shouldHideBottomBar = currentRoute.isTopLevel == true
   // val currentSelectedTabRoute = currentDestination?.bottomTabRoute() ?: homeGraphRoute

//    val navAnim = remember(currentRoute, prevRoute, currentDestination, previousDestination) {
//        determineNavAnimation(
//            to = currentRoute,
//            from = prevRoute,
//            currentDestination = currentDestination,
//            previousDestination = previousDestination
//        )
//    }

//    LaunchedEffect(currentRoute, currentDestination) {
//        Timber.d("""
//                ------------------------------------------------------------------
//                currentRoute: ${currentRoute.route}
//                isTopLevel: ${currentRoute.isTopLevel}
//                currentDestination: ${currentDestination?.route}
//                ------------------------------------------------------------------
//            """.trimIndent())
//    }


//    LaunchedEffect(Unit) {
//        SnackBarPresenter
//            .messagesFlow
//            .observeWithLifecycle(lifecycleOwner.lifecycle) {
//                val result = snackbarHostState.showSnackbar(
//                    PodawanSnackbarVisuals(
//                        message = it.message,
//                        isDebug = it.isDebug,
//                        withDismissAction = !it.autoDismiss,
//                        duration = if (it.autoDismiss) SnackbarDuration.Short else SnackbarDuration.Indefinite,
//                        title = it.title,
//                        actionLabel = it.actionLabel
//                    )
//                )
//
//                if (result == SnackbarResult.ActionPerformed) {
//                    it.action?.invoke()
//                }
//            }
//    }

    PodawanTheme {
        Screen(
            snackbarHost = {
//                SnackbarHost(
//                    hostState = snackbarHostState,
//                    snackbar = { snackbarData ->
//                        (snackbarData.visuals as? PodawanSnackbarVisuals)?.let {
//                            val data = remember {
//                                podawanSnackbarData(
//                                    visuals = it,
//                                    onAction = snackbarData::performAction,
//                                    onDismiss = snackbarData::dismiss
//                                )
//                            }
//                            Snackbar(
//                                podawanSnackbarData = data
//                            )
//                        }
//                    }
//                )
            },
            bottomBar = {
//                AnimatedVisibility(
//                    visible = !shouldHideBottomBar,
//                    enter = expandVertically(),
//                    exit = shrinkVertically(),
//                ) {
//                    AppBottomBar(
//                        currentTabRoute = homeGraphRoute,
//                        onItemClick = { item ->
//                            delegatingRouter.navigate(
//                                fillRoute(item) {
//                                    popUpTo(
//                                        id = navController.graph.findStartDestination().id,
//                                        saveState = true
//                                    )
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            )
//                        }
//                    )
//                }
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                //OfflineBar(isOffline
                //DownloadProgressBar(updateStatus)

                LaunchedEffect(navController) {
                    Log.d("Elijah", "nav controller CHANGED")
                }

                LaunchedEffect(startingRoute) {
                    Log.d("Elijah", "startingRoute CHANGED")
                }

                LaunchedEffect(navGraphRegistry) {
                    Log.d("Elijah", "navGraphRegistry CHANGED")
                }
                NavHost(
                    //modifier = Modifier.padding(it),
                    navController = navController,
                    startDestination = startingRoute.route,
//                    enterTransition = navAnim.enter,
//                    exitTransition = navAnim.exit,
//                    popEnterTransition = navAnim.popEnter,
//                    popExitTransition = navAnim.popExit
                ) {
                    // the base screens of a tab are never navigated to, so they are never added to the
                    // list inside the router that allows us to track the current route
                    // which we use for determining the nav animation. So we need to manually add them
//                    actualRouter.addRoutes(
//                        listOf(
//                            startingRoute,
//                            feedRoute.noArgRoute(navAnimType = NavAnimType.None),
//                            searchRoute.noArgRoute(navAnimType = NavAnimType.None),
//                            libraryRoute.noArgRoute(navAnimType = NavAnimType.None)
//                        )
//                    )

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

//                    delegatingRouter.setRouter(
//                        router = actualRouter,
//                        lifecycle = lifecycle,
//                        startingRoute = startingRoute
//                    )
                }

                val floatingWindowNavigator = remember {
                    navController.getFloatingWindowNavigator()
                }

                floatingWindowNavigator?.let {
                    FloatingWindowHost(floatingWindowNavigator)
                }
            }
        }
    }
}
