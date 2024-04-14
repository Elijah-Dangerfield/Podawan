package com.dangerfield.libraries.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.get
import com.dangerfield.libraries.navigation.floatingwindow.FloatingWindowNavigator
import podawan.core.Catching
import podawan.core.logOnFailure
import podawan.core.throwIfDebug

fun NavHostController.getFloatingWindowNavigator(): FloatingWindowNavigator? = Catching {
    navigatorProvider.get<Navigator<out NavDestination>>(
        FloatingWindowNavigator.NAME
    ) as? FloatingWindowNavigator ?: throw IllegalStateException("No floating window navigator")
}
    .logOnFailure("No floating window navigator found")
    .throwIfDebug()
    .getOrNull()
