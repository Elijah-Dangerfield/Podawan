package com.dangerfield.libraries.navigation.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.navigation.BuildNavHost
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.route
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Named

@Stable
@AutoBind
@Named("SingleGraphNavHost")
class SingleGraphNavHost @Inject constructor(
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
                coroutineScope = coroutineScope,
                startingRoute = startingRoute
            )
        }

        val currentFilledRoute by delegatingRouter.currentRouteFlow.collectAsStateWithLifecycle(
            startingRoute
        )
        val shouldHideBottomBar = currentFilledRoute.isTopLevel == true
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        /*
        The problem with single is that I cant do the whole: is bottom bar, to determine what
        nav anim to play. AGH.
         */
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
                                route = feedRoute.navRoute,
                                isSelected = selectedTabIndex == 0,
                                selectedIcon = PodawanIcon.HomeFilled("Home"),
                                unselectedIcon = PodawanIcon.HomeOutline("Home")
                            ),
                            BottomBarItem(
                                title = "Search",
                                route = searchRoute.navRoute,
                                selectedIcon = PodawanIcon.SearchFilled("Search"),
                                unselectedIcon = PodawanIcon.SearchOutline("Search"),
                                badgeAmount = 2,
                                isSelected = selectedTabIndex == 1
                            ),
                            BottomBarItem(
                                title = "Library",
                                route = libraryRoute.navRoute,
                                selectedIcon = PodawanIcon.LibraryFilled("Library"),
                                unselectedIcon = PodawanIcon.LibraryOutline("Library"),
                                isSelected = selectedTabIndex == 2
                            )
                        ),
                        onItemClick = { item ->

                            when (item.route) {
                                feedRoute.navRoute -> selectedTabIndex = 0
                                searchRoute.navRoute -> selectedTabIndex = 1
                                libraryRoute.navRoute -> selectedTabIndex = 2
                            }

                            actualRouter.navigate(
                                fillRoute(route(item.route)) {

                                    navAnimType = NavAnimType.None

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
            NavHost(
                modifier = Modifier.padding(it),
                navController = actualRouter.navHostController,
                startDestination = startingRoute.route,
                enterTransition = currentFilledRoute.navAnimType.enterTransition,
                exitTransition = currentFilledRoute.navAnimType.exitTransition,
                popEnterTransition = currentFilledRoute.navAnimType.popEnterTransition,
                popExitTransition = currentFilledRoute.navAnimType.popExitTransition
            ) {

                delegatingRouter.setRouter(
                    router = actualRouter,
                    lifecycle = lifecycle,
                    startingRoute = startingRoute
                )

                // add al destinations to this single graph
                navGraphRegistry.addTopLevelDestinations(this)
                navGraphRegistry.addHomeDestinations(this)
                navGraphRegistry.addSearchDestinations(this)
                navGraphRegistry.addLibraryDestinations(this)
            }
        }
    }
}
