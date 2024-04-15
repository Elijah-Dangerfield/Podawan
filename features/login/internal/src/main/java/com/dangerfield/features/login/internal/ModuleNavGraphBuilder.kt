package com.dangerfield.features.login.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.login.internal.LoginViewModel.Action.Login
import com.dangerfield.features.login.internal.LoginViewModel.Action.UpdateEmail
import com.dangerfield.features.login.internal.LoginViewModel.Action.UpdatePassword
import com.dangerfield.features.login.loginRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.floatingwindow.dialog
import com.dangerfield.libraries.navigation.navArgument
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : GlobalNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = loginRoute.navRoute,
            arguments = loginRoute.navArguments
        ) {
            val viewmodel = hiltViewModel<LoginViewModel>()
            val state by viewmodel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewmodel.eventFlow) {
                when (it) {
                    is LoginViewModel.Event.LoginFailed -> router.navigateToLoginError(it.errorClass)
                }
            }

            LoginScreen(
                emailFieldState = state.emailFieldState,
                passwordFieldState = state.passwordFieldState,
                isFormValid = state.isFormValid,
                onEmailChanged = { viewmodel.takeAction(UpdateEmail(it)) },
                onPasswordChanged = { viewmodel.takeAction(UpdatePassword(it)) },
                onLoginClicked = { viewmodel.takeAction(Login) },
                isLoading = state.isLoading
            )
        }

        dialog(
            route = loginErrorDialogRoute.navRoute,
            arguments = loginErrorDialogRoute.navArguments
        ) {
            val errorClass = it.navArgument<String>(errorClassArgument)

            LoginFailedDialog(
                onDismiss = { router.goBack() },
                errorClass = errorClass
            )
        }
    }
}