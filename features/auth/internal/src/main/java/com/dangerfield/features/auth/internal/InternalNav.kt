package com.dangerfield.features.auth.internal

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

val errorClassArgument = navArgument("error_class") {
    type = NavType.StringType
    nullable = true
}

val authenticationError = route("auth_error_dialog") {
    argument(errorClassArgument)
}

val signupRoute = route("signup", isTopLevel = true)

fun Router.navigateToSignup() {
    navigate(signupRoute.noArgRoute())
}

fun Router.navigateToAuthenticationError(errorClass: String?) {
    navigate(
        fillRoute(authenticationError) {
            fillArg(errorClassArgument, errorClass)
        }
    )
}