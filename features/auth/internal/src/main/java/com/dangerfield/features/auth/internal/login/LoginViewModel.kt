package com.dangerfield.features.auth.internal.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.auth.internal.IsValidEmail
import com.dangerfield.features.auth.internal.login.LoginViewModel.Action
import com.dangerfield.features.auth.internal.login.LoginViewModel.Event
import com.dangerfield.features.auth.internal.login.LoginViewModel.State
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.session.LoginResult
import com.dangerfield.libraries.session.UserRepository
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.FieldState.Idle
import com.dangerfield.libraries.ui.FieldState.Invalid
import com.dangerfield.libraries.ui.FieldState.Valid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val isValidEmail: IsValidEmail,
) : SEAViewModel<State, Event, Action>(
    savedStateHandle = savedStateHandle,
    initialStateArg = State()
) {

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.UpdateEmail -> action.updateEmail()
            is Action.UpdatePassword -> action.updatePassword()
            is Action.Login -> action.login()
        }
    }

    private suspend fun Action.UpdateEmail.updateEmail() {
        clearFormError()
        // eagerly validate, but debounce the invalidation
        if (isValidEmail(email)) {
            updateState { it.copy(emailFieldState = Valid(email)) }
        } else {
            updateState { it.copy(emailFieldState = Idle(email)) }
        }

        updateStateDebounced(duration = 1.seconds) {
            when {
                email.isEmpty() -> it.copy(emailFieldState = Idle(email))
                isValidEmail(email) -> it.copy(emailFieldState = Valid(email))
                else -> {
                    it.copy(emailFieldState = Invalid(email, "Please enter a valid email"))
                }
            }
        }

    }

    private suspend fun Action.UpdatePassword.updatePassword() {
        // eagerly validate, but debounce the invalidation
        if (password.isValidPassword()) {
            updateState { it.copy(passwordFieldState = Valid(password)) }
        } else {
            updateState { it.copy(passwordFieldState = Idle(password)) }
        }

        updateStateDebounced(duration = 1.seconds) {
            if (password.isValidPassword()) {
                it.copy(passwordFieldState = Valid(password))
            } else {
                it.copy(
                    passwordFieldState = Invalid(
                        password,
                        "Password must be at least 6 characters"
                    )
                )
            }
        }
    }

    private suspend fun Action.Login.login() {
        if (!state.isFormValid) {
            Timber.d("Log inForm is not valid when submitting")
        } else {
            updateState { it.copy(isLoading = true) }

            val email = state.emailFieldState.value ?: return
            val password = state.passwordFieldState.value ?: return

            userRepository.login(email, password)
                .onSuccess { result ->
                    when (result) {
                        is LoginResult.Success -> {
                            // keep the screen as loading. App state update should navigate the user
                            // we wait for app state to update before inferring failure
                            viewModelScope.launch {
                                delay(10.seconds)
                                updateState { it.copy(isLoading = false) }
                                sendEvent(Event.LoginFailed(null))
                            }
                        }

                        is LoginResult.InvalidCredentials -> {

                            updateState {
                                it.copy(
                                    isLoading = false,
                                    passwordFieldState = FieldState.Error(
                                        it.passwordFieldState.value.orEmpty(),
                                        "Hmmm, these credentials don't seem to match our records. Please try again."
                                    )
                                )
                            }
                        }
                    }
                }
                .onFailure {
                    updateState { it.copy(isLoading = false) }
                    Log.d("Elijah", "Got log in error")
                    sendEvent(Event.LoginFailed(it.javaClass.simpleName))
                }
        }
    }

    private fun clearFormError() {
        if (state.passwordFieldState is FieldState.Error) {
            takeAction(Action.UpdatePassword(state.passwordFieldState.value.orEmpty()))
        }
    }

    private fun String.isValidPassword(): Boolean {
        return this.length >= 6
    }

    override suspend fun mapEachState(state: State): State {
        return state.copy(
            isFormValid = state.emailFieldState is Valid && state.passwordFieldState is Valid
        )
    }

    data class State(
        val emailFieldState: FieldState<String> = Idle(""),
        val passwordFieldState: FieldState<String> = Idle(""),
        val isFormValid: Boolean = false,
        val isLoading: Boolean = false
    )

    sealed class Action {
        data class UpdateEmail(val email: String) : Action()
        data class UpdatePassword(val password: String) : Action()
        object Login : Action()
    }

    sealed class Event {
        class LoginFailed(val errorClass: String?) : Event()
    }
}