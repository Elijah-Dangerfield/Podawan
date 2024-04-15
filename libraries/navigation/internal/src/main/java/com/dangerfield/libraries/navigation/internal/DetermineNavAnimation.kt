package com.dangerfield.libraries.navigation.internal

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fadeInToEndAnim
import com.dangerfield.libraries.navigation.fadeOutToStart
import com.dangerfield.libraries.navigation.noAnimEnter
import com.dangerfield.libraries.navigation.noAnimExit
import javax.inject.Inject

class DetermineNavAnimation @Inject constructor() {

    operator fun invoke(
        to: Route.Filled,
        from: Route.Filled?
    ): NavAnimation {
        // whoever is leaving should leave based on the new entrance
        val exitAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
            when (to.navAnimType) {
                NavAnimType.None -> { { noAnimExit() }
                }

                NavAnimType.FadeIn -> {
                    // if new screen is fading in, the previous screen should fade out
                    { fadeOut() }
                }

                NavAnimType.SlideIn -> {
                    // if new one is sliding in, the previous should slide out
                    { fadeOutToStart() }
                }

                NavAnimType.SlideOver -> {
                    // if new one is sliding over, the previous one should stay put
                    { noAnimExit() }
                }
            }


        // if the screen we are going to RIGHT NOW, is entering do to the prev being popped, then we will play this on the current
        val currentReenterAnim: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
                = when (from?.navAnimType) {
            // if previous screen had no enter/exit anim, then we should have no enter anim
            NavAnimType.None -> {
                { noAnimEnter() }
            }

            // if previous screen faded out, then we should fade in
            NavAnimType.FadeIn -> {
                { fadeIn() }
            }

            // if previous screen slid out, then we should slide in
            NavAnimType.SlideIn -> {
                { fadeInToEndAnim() }
            }

            // if previous screen slide over, then itll side back out, dont anim previous screen
            NavAnimType.SlideOver -> {
                { noAnimEnter() }
            }

            null ->  { { noAnimEnter() } }
        }

        return NavAnimation(
            enter = to.navAnimType.enterTransition,
            exit = exitAnim,
            popEnter = currentReenterAnim,
            popExit = from?.navAnimType?.popExitTransition ?: to.navAnimType.popExitTransition
        )
    }
}


data class NavAnimation(
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
)