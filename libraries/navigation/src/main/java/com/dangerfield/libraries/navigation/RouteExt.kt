package com.dangerfield.libraries.navigation

import androidx.navigation.NavOptions

fun Route.Filled.navOptions(): NavOptions {
    val builder = NavOptions.Builder()

    this.popUpTo?.let {
        val id = it.popUpToId

        if (id != null) {
            builder.setPopUpTo(
                destinationId = id,
                inclusive = it.popUpToInclusive,
                saveState = it.saveState
            )
        } else {
            builder.setPopUpTo(
                route = it.popUpToRoute,
                inclusive = it.popUpToInclusive,
                saveState = it.saveState
            )
        }
    }

    this.restoreState?.let {
        builder.setRestoreState(it)
    }

    this.isLaunchSingleTop?.let {
        builder.setLaunchSingleTop(it)
    }

    return builder.build()
}

