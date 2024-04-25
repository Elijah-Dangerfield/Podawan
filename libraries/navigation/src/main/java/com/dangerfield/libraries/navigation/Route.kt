package com.dangerfield.libraries.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.parcelize.Parcelize
import podawan.core.Catching
import podawan.core.checkInDebug
import podawan.core.doNothing
import podawan.core.logOnFailure
import podawan.core.throwIfDebug
import timber.log.Timber

/**
 * Route is a class that represents a destination in a navigation graph.
 * It can be used to define that destination (A Template) and it can be used to navigate to that
 * destination. You can only use a Filled Route to navigate to a destination.
 *
 * Templates represent a declaration of a route to be filled when navigated to. Its the empty bucket
 * that you fill with arguments when you want to navigate to a destination.
 *
 * Filled represents a route that has been filled with arguments and is ready to be navigated to.
 */
class Route internal constructor() {

    class Builder internal constructor() {

        private var baseRoute: String = ""

        // These are baked into the route so that the NavHost can use them to determine animations
        // and bottom bar visibility.
        private val arguments = mutableListOf<NamedNavArgument>(
            isTopLevelArg,
            isLaunchSingleTopArg,
            restoreStateArg,
            navAnimTypeArg
        )
        private var deepLinks = mutableListOf<NavDeepLink>()
        private var isTopLevel: Boolean? = null
        private var navAnimType: NavAnimType? = null

        fun route(
            route: String,
        ): Builder {
            this.baseRoute = route
            return this
        }

        fun deepLink(link: NavDeepLink): Builder {
            deepLinks.add(link)
            return this
        }

        fun isTopLevel(value: Boolean = true): Route.Builder {
            isTopLevel = value
            return this
        }

        fun navAnimType(value: NavAnimType): Route.Builder {
            navAnimType = value
            return this
        }

        fun argument(name: String, type: NavType<*>, default: Any? = null): Route.Builder {
            val argument = navArgument(name) {
                this.type = type
                if (default != null) {
                    this.defaultValue = default
                }
            }
            arguments.add(argument)
            return this
        }

        fun nullableArgument(name: String, type: NavType<*>, default: Any? = null): Route.Builder {
            val argument = navArgument(name) {
                this.type = type
                this.nullable = true
                if (default != null) {
                    this.defaultValue = default
                }
            }
            arguments.add(argument)
            return this
        }

        fun argument(name: String, builder: NavArgumentBuilder.() -> Unit): Route.Builder {
            val argument = navArgument(name, builder)
            arguments.add(argument)
            return this
        }

        fun argument(namedNavArgument: NamedNavArgument): Route.Builder {
            arguments.add(namedNavArgument)
            return this
        }

        fun build(): Template {
            checkForDuplicateArguments()
            checkForUndefinedRoute()

            val args = arguments.distinctBy { it.name }

            val templatedRoute =
                args.fold(StringBuilder(baseRoute)) { routeBuilder, argument ->
                    if (routeBuilder.contains("?")) {
                        // not the first argument, use &
                        routeBuilder.append("&${argument.name}={${argument.name}}")
                    } else {
                        // first argument, use ?
                        routeBuilder.append("?${argument.name}={${argument.name}}")
                    }
                }

            if (templatedRoute.toString().contains("login")) {
                Timber.d("building template with route: ${templatedRoute.toString()}")
                Timber.d("Arugments were: ${args.joinToString(" | ") { it.name}}")
            }

            return Template(
                navRoute = templatedRoute.toString(),
                navArguments = args.toList(),
                navAnimType = navAnimType,
                deepLinks = deepLinks,
                isTopLevel = isTopLevel,
            )
        }

        private fun checkForUndefinedRoute() {
            checkInDebug(baseRoute.isNotBlank()) { "Base route cannot be blank" }
        }

        private fun checkForDuplicateArguments() {
            val duplicateArguments = arguments.groupBy { it.name }
                .filter { it.value.size > 1 }
                .map { it.key }

            checkInDebug(duplicateArguments.isEmpty()) {
                "Duplicate arguments in route: $baseRoute found: $duplicateArguments"
            }
        }
    }

    /*
    Immutable guarantees that the params of Template wont change but that instaad we would
    just create a new object.

    We can only create templates through the Builder, which returns a new template
    when you build. This guarantee is safe.
     */
    @Immutable
    data class Template internal constructor(
        val navRoute: String,
        val navArguments: List<NamedNavArgument>,
        val deepLinks: List<NavDeepLink>,
        val isTopLevel: Boolean?,
        val navAnimType: NavAnimType? = null
    ) {

        class Filler internal constructor(
            val navRoute: String,
            val navArguments: List<NamedNavArgument> = emptyList(),
            var isTopLevel: Boolean = isTopLevelDefault,
            var navAnimType: NavAnimType = navAnimTypeDefault,
            private var popUpTo: NavPopUp? = null,
            var launchSingleTop: Boolean? = null,
            var restoreState: Boolean? = null
        ) {

            private val filledRouteBuilder = StringBuilder(navRoute)

            fun arg(argument: NamedNavArgument, value: Any?): Filler {

                checkInDebug(navArguments.contains(argument)) {
                    "Tried to fill argument ${argument.name} with value $value, but route $navRoute does not contain this argument."
                }

                checkInDebug(
                    value != null
                            || argument.argument.isNullable
                            || argument.argument.isDefaultValuePresent
                ) {
                    "Route $navRoute does not support null values for argument ${argument.name}."
                }

                Catching {
                    when {
                        !navArguments.contains(argument) -> {
                            Timber.d("argument ${argument.name} NOT FOUND in Route ${navRoute}")
                            doNothing()
                        }
                        value == null -> {
                            Timber.d("argument ${argument.name} is null. Removing from route.")
                            filledRouteBuilder.removeUnfilledArg(argument)
                            Timber.d("Resulting route: $filledRouteBuilder")
                        }
                        filledRouteBuilder.filledArgumentExists(argument) -> {
                            throwIfDebug {
                                "Argument ${argument.name} already filled in route $navRoute"
                            }
                            doNothing()
                        }
                        else -> {
                            Timber.d("Filling Argument ${argument.name } with value $value in route $navRoute")
                            filledRouteBuilder.fillArgument(argument, value)
                            Timber.d("Resulting route: $filledRouteBuilder")
                        }
                    }
                }
                    .logOnFailure("Error filling route $navRoute with argument ${argument.name}")
                    .throwIfDebug()

                return this
            }

            fun launchSingleTop(value: Boolean = true): Filler {
                launchSingleTop = value
                return this
            }

            fun restoreState(value: Boolean = true): Filler {
                restoreState = value
                return this
            }

            fun popUpTo(route: String?, inclusive: Boolean = false, saveState: Boolean = false): Filler {
                popUpTo = NavPopUp(route, popUpToInclusive = inclusive, saveState = saveState)
                return this
            }

            fun popUpTo(id: Int, inclusive: Boolean = false, saveState: Boolean = false): Filler {
                popUpTo = NavPopUp(
                    popUpToRoute = null,
                    popUpToId = id,
                    popUpToInclusive = inclusive,
                    saveState = saveState
                )
                return this
            }

            fun isTopLevel(value: Boolean = true): Filler {
                isTopLevel = value
                return this
            }

            fun navAnimType(value: NavAnimType): Filler {
                navAnimType = value
                return this
            }

            fun args(vararg args: Pair<NamedNavArgument, Any?>): Filler {
                args.forEach { (argument, value) ->
                    arg(argument, value)
                }
                return this
            }

            fun build(): Filled {
                // if these values are null, then the args will be removed from the route
                // which will cause either the default or null to be used when extracting
                arg(isTopLevelArg, isTopLevel)
                arg(isLaunchSingleTopArg, launchSingleTop)
                arg(restoreStateArg, restoreState)
                arg(navAnimTypeArg, navAnimType)

                navArguments.forEach {
                    val isNotFilled = filledRouteBuilder.unfilledArgumentExists(it)

                    if (isNotFilled) {
                        throwIfDebug { "Route $navRoute was not filled with argument ${it.name}" }

                        Catching {
                            // if the argument was not filled, remove it from the route, nav library
                            // will handle using defaults or null
                            filledRouteBuilder.removeUnfilledArg(it)
                        }
                            .logOnFailure()
                            .throwIfDebug()
                    }
                }

                return Filled(
                    route = filledRouteBuilder.toString(),
                    popUpTo = popUpTo,
                    isTopLevel = isTopLevel,
                    isLaunchSingleTop = launchSingleTop,
                    navAnimType = navAnimType,
                    restoreState = restoreState,
                )
            }
        }
    }

    @Immutable
    @Parcelize
    data class Filled internal constructor(
        val route: String,
        val popUpTo: NavPopUp? = null,
        val isLaunchSingleTop: Boolean? = null,
        val navAnimType: NavAnimType,
        val isTopLevel: Boolean,
        val restoreState: Boolean? = null
    ) : Parcelable
}

val isLaunchSingleTopArg = navArgument("isLaunchSingleTop") {
    type = NavType.BoolType
    defaultValue = true
}

val restoreStateArg = navArgument("restoreState") {
    type = NavType.BoolType
    defaultValue = false
}

val isTopLevelArg = navArgument("isTopLevel") {
    type = NavType.BoolType
    defaultValue = isTopLevelDefault
}
internal const val isTopLevelDefault = false

val navAnimTypeArg = navArgument("navAnimType") {
    type = NavType.EnumType(NavAnimType::class.java)
}

val navAnimTypeDefault = NavAnimType.SlideIn


/**
 * Fills a route with the applied block
 * favors template declared values if they exist
 */
fun Route.Template.fill(
    block: Route.Template.Filler.() -> Unit = {}
): Route.Filled {
    val filler = Route.Template.Filler(
        navRoute = navRoute,
        navArguments = navArguments,
    )

    filler.block()

    // if the route was declared as top level, all navigation to it must be top level,
    // so ignore filled values in favor of declared values
    isTopLevel?.let { filler.isTopLevel = it }

    // if the route was declared with a nav anim type, all navigation to it must use that anim,
    // so ignore filled values in favor of declared values
    navAnimType?.let { filler.navAnimType = it }

    return filler.build()
}

fun route(
    name: String,
    block: Route.Builder.() -> Unit = {}
): Route.Template {
    val builder = Route.Builder()
    builder.route(name)
    builder.block()
    return builder.build()
}
