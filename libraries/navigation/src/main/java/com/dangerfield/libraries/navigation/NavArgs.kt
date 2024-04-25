package com.dangerfield.libraries.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import podawan.core.Catching
import podawan.core.checkInDebug
import podawan.core.debugSnackOnError
import podawan.core.logOnFailure

/**
 * Gets a nav argument from a Bundle
 *
 * @param navArgument the argument to get
 * @param checkExists whether to check if the argument exists in the SavedStateHandle
 */
fun <T : Any> Bundle.navArgument(navArgument: NamedNavArgument, checkExists: Boolean = true): T? = Catching {
    val value = get(navArgument.name)
    if (checkExists) {
        // check if the argument exists in the saved state handle. Only runs in debug.
        // Throws error if not found and the arg doesnt have a default or is nullable
        checkInDebug(value != null
                || navArgument.argument.isNullable
                || navArgument.argument.defaultValue != null
        ) {
            "Argument ${navArgument.name} is not nullable/default-able but was not found in SavedStateHandle"
        }
    }

    value as T
}
    .logOnFailure()
    .getOrNull()
