package com.dangerfield.libraries.navigation

import android.os.Parcelable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import kotlinx.android.parcel.Parcelize

@Parcelize
class NavAnimBuilder : Parcelable {
    var enterTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeInToStartAnim()
    }

    var exitTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOutToStart()
    }

    var popEnterTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeInToEndAnim()
    }

    var popExitTransition : AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOutToEndAnim()
    }
}