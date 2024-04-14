package com.dangerfield.libraries.navigation.internal

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.login.loginRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.navigation.BuildNavHost
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fadeInToEndAnim
import com.dangerfield.libraries.navigation.fadeInToStartAnim
import com.dangerfield.libraries.navigation.fadeOutToEndAnim
import com.dangerfield.libraries.navigation.fadeOutToStartAnim
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowHost
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import com.dangerfield.libraries.navigation.getFloatingWindowNavigator
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.text.Text
import se.ansman.dagger.auto.AutoBind
import timber.log.Timber
import javax.inject.Inject

@Stable
class BuildNavHostImpl @Inject constructor(
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

        Timber.d("Setting navigation up with starting route: $startingRoute")

        RootGraph(
            isLoggedIn = false
        )
    }
}


@Composable
fun RootGraph(
    isLoggedIn: Boolean
) {
    val rootRouter = rememberNavController(FloatingWindowNavigator())

    NavHost(
        navController = rootRouter,
        startDestination = if (isLoggedIn) "main" else "auth",
        enterTransition = { fadeInToStartAnim() },
        exitTransition = { fadeOutToStartAnim() },
        popEnterTransition = { fadeInToEndAnim() },
        popExitTransition = { fadeOutToEndAnim() }
    ) {
        AuthGraph()
        MainGraph()
    }
}

fun NavGraphBuilder.AuthGraph() {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable("login") {
            Screen {
                Text("Login")
            }
        }
    }
}

fun NavGraphBuilder.MainGraph() {
    composable("main") {
        val mainRouter = rememberNavController(FloatingWindowNavigator())
        Screen(
            bottomBar = {
                BottomBar(
                    items = listOf(
                        BottomBarItem.Home(true),
                        BottomBarItem.Search(false),
                        BottomBarItem.Library(false)
                    ),
                    onItemClick = { item ->
                        mainRouter.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(mainRouter.graph.findStartDestination().id) {
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
        ) {
            NavHost(
                navController = mainRouter,
                startDestination = "homeGraph",
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {

                navigation(route = "homeGraph", startDestination = "feed") {
                    composable("feed") {
                        Screen {
                            Text("Feed")
                        }
                    }
                }

                navigation(route = "searchGraph", startDestination = "search") {
                    composable("search") {
                        Screen {
                            Text("Search")
                        }
                    }
                }

                navigation(route = "libraryGraph", startDestination = "library") {
                    composable("library") {
                        Screen {
                            Text("Library")
                        }
                    }
                }
            }
        }
    }
}











