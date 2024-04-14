package com.dangerfield.features.login.internal

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.fillRoute
import com.dangerfield.libraries.navigation.route

val errorClassArgument = navArgument("error_class") {
    type = NavType.StringType
    nullable = true
}

val loginErrorDialogRoute = route("login_error_dialog") {
    argument(errorClassArgument)
}

fun Router.navigateToLoginError(errorClass: String?) {
    navigate(
        fillRoute(loginErrorDialogRoute) {
            fill(errorClassArgument, errorClass)
        }
    )
}