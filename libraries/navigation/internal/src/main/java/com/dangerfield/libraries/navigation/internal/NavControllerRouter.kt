package com.dangerfield.libraries.navigation.internal

import android.net.Uri
import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import podawan.core.Catching
import podawan.core.debugSnackOnError
import podawan.core.logOnFailure
import podawan.core.throwIfDebug

class NavControllerRouter(
    val navHostController: NavHostController,
    private val coroutineScope: CoroutineScope,
    private val startingRoute: Route.Filled
) : Router {

    private val _currentRouteFlow: MutableSharedFlow<Route.Filled> = MutableStateFlow(startingRoute)
    private val routeToFilled = mutableMapOf<String, Route.Filled>()

    override val currentRouteFlow = _currentRouteFlow

    fun addRoutes(list: List<Route.Filled>) {
        list.forEach {
            routeToFilled[it.route] = it
        }
    }

    init {
        navHostController
            .currentBackStackEntryFlow
            .mapNotNull {
                val route = it.destination.route
                routeToFilled[route]
            }.collectIn(coroutineScope) {
                _currentRouteFlow.emit(it)
            }
    }

    override fun navigate(filledRoute: Route.Filled) {
        Catching {
            val navOptions = filledRoute.navOptions()
            routeToFilled[filledRoute.route] = filledRoute
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