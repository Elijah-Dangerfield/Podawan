package com.dangerfield.libraries.navigation

import android.os.Parcelable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NavAnimType(
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
) : Parcelable {

     SlideIn(
        enterTransition =  { fadeInToStartAnim() },
        exitTransition = { fadeOutToStart() },
        popEnterTransition = { fadeInToEndAnim() },
        popExitTransition = { fadeOutToEndAnim() }
    ),

    SlideUp(
        enterTransition =  { slideUpToEnterBottomSheet() },
        exitTransition = { slideDownToExitBottomSheet() },
        popEnterTransition = { noAnimEnter() },
        popExitTransition = { slideDownToExitBottomSheet() }
    ),

     None(
        enterTransition =  { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ),

   FadeIn(
        enterTransition =  { fadeIn(animationSpec = tween(600)) },
        exitTransition = { fadeOut(animationSpec = tween(600)) },
        popEnterTransition = { fadeIn(animationSpec = tween(600)) },
        popExitTransition = { fadeOut(animationSpec = tween(600)) }
    )
}