package com.dangerfield.features.auth.internal

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.auth.internal.login.LoginViewModel.Action.Login
import com.dangerfield.features.auth.internal.login.LoginViewModel.Action.UpdateEmail
import com.dangerfield.features.auth.internal.login.LoginViewModel.Action.UpdatePassword
import com.dangerfield.features.auth.internal.login.LoginScreen
import com.dangerfield.features.auth.internal.login.LoginViewModel
import com.dangerfield.features.auth.internal.signup.SignupScreen
import com.dangerfield.features.auth.internal.signup.SignupViewModel
import com.dangerfield.features.auth.loginRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.dialog
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
                    is LoginViewModel.Event.LoginFailed -> router.navigateToAuthenticationError(it.errorClass)
                }
            }

            LoginScreen(
                emailFieldState = state.emailFieldState,
                passwordFieldState = state.passwordFieldState,
                isFormValid = state.isFormValid ,
                onEmailChanged = { viewmodel.takeAction(UpdateEmail(it)) },
                onPasswordChanged = { viewmodel.takeAction(UpdatePassword(it)) },
                onLoginClicked = { viewmodel.takeAction(Login) },
                isLoading = state.isLoading,
                onSignupClicked = router::navigateToSignup
            )
        }

        composable(
            route = signupRoute.navRoute,
            arguments = signupRoute.navArguments
        ) {
            val viewmodel = hiltViewModel<SignupViewModel>()
            val state by viewmodel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewmodel.eventFlow) {
                when (it) {
                    is SignupViewModel.Event.SignupFailed -> router.navigateToAuthenticationError(it.errorClass)
                }
            }

            SignupScreen(
                emailFieldState = state.emailFieldState,
                passwordFieldState = state.passwordFieldState,
                isFormValid = state.isFormValid,
                onEmailChanged = { viewmodel.takeAction(SignupViewModel.Action.UpdateEmail(it)) },
                onPasswordChanged = { viewmodel.takeAction(SignupViewModel.Action.UpdatePassword(it)) },
                onSignupClicked = { viewmodel.takeAction(SignupViewModel.Action.Signup) },
                isLoading = state.isLoading,
                onBackClicked = { router.goBack() }
            )
        }

        dialog(
            route = authenticationError.navRoute,
            arguments = authenticationError.navArguments
        ) {

            val errorClass = it.navArgument<String>(errorClassArgument)

            AuthenticationFailedDialog(
                onDismiss = { router.goBack() },
                errorClass = errorClass
            )
        }
    }
}