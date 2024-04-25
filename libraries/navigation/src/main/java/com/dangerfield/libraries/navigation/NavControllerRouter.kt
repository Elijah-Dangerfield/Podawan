package com.dangerfield.libraries.navigation

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import podawan.core.Catching
import podawan.core.debugSnackOnError
import podawan.core.logOnFailure
import podawan.core.throwIfDebug
import timber.log.Timber

class NavControllerRouter(
    val navHostController: NavHostController,
    private val coroutineScope: CoroutineScope,
) : Router {

    private val _currentRouteFlow: MutableSharedFlow<RouteInfo> = MutableSharedFlow(replay = 1)
    override val currentRouteInfo = _currentRouteFlow.distinctUntilChanged()
    private val startingRoutes = mutableMapOf<String, Route.Filled>()

    init {
        navHostController
            .currentBackStackEntryFlow
            .mapNotNull {
                val route = it.destination.route
                val filledRoute = startingRoutes[route]

                if (filledRoute != null) {
                    Timber.d("Found starting route $route, getting route into from it")
                }

                filledRoute?.asRouteInfo() ?: it.asRouteInfo()
            }
            .collectIn(coroutineScope) {
                _currentRouteFlow.emit(it)
            }
    }

    /**
     * With starting routes the caller cannot specify how to fill out the route when opening it,
     * they can only declare the destination to land on. In order specify the behavior of a starting
     * destination we keep a mapping of the Routes Template to the Filled Route.
     */
    fun registerStartingRoutes(vararg routePairs: Pair<Route.Template, Route.Filled>) {
        routePairs.forEach {
            startingRoutes[it.first.navRoute] = it.second
        }
    }

    override fun navigate(filledRoute: Route.Filled) {
        Catching {
            val navOptions = filledRoute.navOptions()
            navHostController.navigate(filledRoute.route, navOptions)
        }
            .logOnFailure()
            .throwIfDebug()
    }

    override fun goBack() {
        Catching {
            navHostController.popBackStack()
        }
            .logOnFailure()
            .throwIfDebug()
    }

    override fun startDestination(): NavDestination {
        return navHostController.graph.findStartDestination()
    }

    override fun openWebLink(url: String, openInApp: Boolean) {
        Catching { Uri.parse(url) }
            .map { uri ->
                // add https if no scheme
                if (uri?.scheme.isNullOrEmpty()) {
                    uri.buildUpon().scheme("https").build()
                } else {
                    uri
                }
            }
            .map {
                if (openInApp) {
                    navHostController.context.openWebLinkFromContext(it)
                } else {
                    navHostController.context.openWebLinkExternally(it)
                }
            }
            .debugSnackOnError { "Could not open web link $url" }
            .logOnFailure()
    }

    override fun popBackTo(
        route: Route.Template,
        inclusive: Boolean
    ) {
        Catching {
            navHostController.popBackStack(route.navRoute, inclusive)
        }
            .logOnFailure()
            .throwIfDebug()

    }

    override fun dismissSheet(sheetState: BottomSheetState) {
        if (sheetState.isVisible) {
            Catching {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    goBack()
                }
            }
                .logOnFailure()
                .throwIfDebug()

        } else {
            goBack()
        }
    }

    override fun ifStillOn(backStackEntry: NavBackStackEntry, action: Router.() -> Unit) {
        if (navHostController.currentDestination?.route == backStackEntry.destination.route) {
            action.invoke(this)
        }
    }

    override fun getBackStackEntry(route: Route.Template): NavBackStackEntry {
        return navHostController.getBackStackEntry(route.navRoute)
    }
}


fun String?.createNavRoute() =
    if (this != null) "android-app://androidx.navigation/$this" else ""
