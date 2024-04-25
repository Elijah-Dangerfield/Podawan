package com.dangerfield.features.auth.internal

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route

val errorClassArgument = navArgument("error_class") {
    type = NavType.StringType
    nullable = true
}

val authenticationError = route("auth_error_dialog") {
    argument(errorClassArgument)
}

val signupRoute = route("signup") {
    isTopLevel(true)
}

fun Router.navigateToSignup() {
    navigate(signupRoute.fill())
}

fun Router.navigateToAuthenticationError(errorClass: String?) {
    navigate(
        authenticationError.fill {
            arg(errorClassArgument, errorClass)
        }
    )
}