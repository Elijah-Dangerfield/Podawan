package com.dangerfield.libraries.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import podawan.core.Catching
import podawan.core.checkInDebug
import podawan.core.doNothing
import podawan.core.logOnFailure
import podawan.core.throwIfDebug

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
        private val arguments = mutableListOf<NamedNavArgument>()
        private var deepLinks = listOf<NavDeepLink>()
        private var isTopLevel: Boolean? = null

        fun route(
            route: String,
            deepLinks: List<NavDeepLink> = emptyList(),
            isTopLevel: Boolean? = null
        ): Builder {
            this.baseRoute = route
            this.deepLinks = deepLinks
            // only assign if passed in, otherwise leave as is
            isTopLevel?.let { this.isTopLevel = it }
            return this
        }

        fun isTopLevel(value: Boolean = true): Route.Builder {
            isTopLevel = value
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

            val safeArgs = arguments.distinctBy { it.name }

            val templatedRoute =
                arguments.fold(StringBuilder(baseRoute)) { routeBuilder, argument ->
                    if (routeBuilder.contains("?")) {
                        // not the first argument, use &
                        routeBuilder.append("&${argument.name}={${argument.name}}")
                    } else {
                        // first argument, use ?
                        routeBuilder.append("?${argument.name}={${argument.name}}")
                    }

                }

            return Template(
                navRoute = templatedRoute.toString(),
                navArguments = safeArgs,
                deepLinks = deepLinks,
                isTopLevel = isTopLevel
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
    class Template internal constructor(
        val navRoute: String,
        val navArguments: List<NamedNavArgument>,
        val deepLinks: List<NavDeepLink>,
        val isTopLevel: Boolean?
    ) {

        fun noArgRoute(singleTop: Boolean = true): Filled {
            val filler = Filler(
                navRoute,
                navArguments,
                deepLinks,
                isTopLevel
            )

            filler.launchSingleTop(singleTop)
            return filler.build()
        }

        class Filler internal constructor(
            val navRoute: String,
            val navArguments: List<NamedNavArgument>,
            val deepLinks: List<NavDeepLink>,
            var isTopLevel: Boolean? = null
        ) {

            private val filledRouteBuilder = StringBuilder(navRoute)
            private var popUpTo: NavPopUp? = null
            private var isLaunchSingleTop: Boolean? = null
            private var restoreState: Boolean? = null
            private var navAnimBuilder: NavAnimBuilder? = null

            fun fill(argument: NamedNavArgument, value: Any?): Filler {

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
                        !navArguments.contains(argument) -> doNothing()
                        value == null -> filledRouteBuilder.removeArgument(argument)
                        else -> filledRouteBuilder.fillArgument(argument, value)
                    }
                }
                    .logOnFailure("Error filling route $navRoute with argument ${argument.name}")
                    .throwIfDebug()

                return this
            }

            fun anim(block: NavAnimBuilder.() -> Unit): Filler {
                if (navAnimBuilder != null) {
                    navAnimBuilder?.apply(block)
                } else {
                    navAnimBuilder = NavAnimBuilder().apply(block)
                }
                return this
            }

            fun launchSingleTop(value: Boolean = true) {
                isLaunchSingleTop = value
            }

            fun restoreState(value: Boolean = true) {
                restoreState = value
            }

            fun popUpTo(route: Route.Template, inclusive: Boolean = false): Filler {
                popUpTo = NavPopUp(route, inclusive)
                return this
            }

            fun topLevel(value: Boolean = true) {
                isTopLevel = value
            }

            fun fill(vararg args: Pair<NamedNavArgument, Any?>): Filler {
                args.forEach { (argument, value) ->
                    fill(argument, value)
                }
                return this
            }

            fun build(): Filled {
                navArguments.forEach {
                    val isNotFilled = filledRouteBuilder.isArgumentNotFilled(it)
                    if (isNotFilled) {
                        throwIfDebug { "Route $navRoute was not filled with argument ${it.name}" }
                    }

                    Catching {
                        filledRouteBuilder.removeArgument(it)
                    }
                        .logOnFailure()
                        .throwIfDebug()
                }

                return Filled(
                    route = filledRouteBuilder.toString(),
                    popUpTo = popUpTo,
                    isTopLevel = isTopLevel,
                    isLaunchSingleTop = isLaunchSingleTop,
                    navAnimBuilder = navAnimBuilder,
                )
            }
        }
    }

    @Immutable
    class Filled internal constructor(
        val route: String,
        val popUpTo: NavPopUp? = null,
        val isLaunchSingleTop: Boolean? = null,
        val navAnimBuilder: NavAnimBuilder? = null,
        val isTopLevel: Boolean? = null,
        val restoreState: Boolean? = null
    )
}

fun fillRoute(
    template: Route.Template,
    block: Route.Template.Filler.() -> Unit
): Route.Filled {
    val filler = Route.Template.Filler(template.navRoute, template.navArguments, template.deepLinks)
    filler.block()
    return filler.build()
}

fun route(block: Route.Builder.() -> Unit): Route.Template {
    val builder = Route.Builder()
    builder.block()
    return builder.build()
}

fun route(
    name: String,
    isTopLevel: Boolean? = null,
    block: Route.Builder.() -> Unit = {}
): Route.Template {
    val builder = Route.Builder()
    builder.block()
    builder.route(name)
    isTopLevel?.let { builder.isTopLevel(it) }
    return builder.build()
}
