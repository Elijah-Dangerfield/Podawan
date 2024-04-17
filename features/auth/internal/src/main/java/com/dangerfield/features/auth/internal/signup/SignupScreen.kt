package com.dangerfield.features.auth.internal.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.components.CircularProgressIndicator
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.button.Button
import com.dangerfield.libraries.ui.components.icon.IconButton
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.text.InputField
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    emailFieldState: FieldState<String>,
    passwordFieldState: FieldState<String>,
    isFormValid: Boolean,
    isLoading: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignupClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = FocusRequester()

    Screen(
        modifier,
        topBar = {
            IconButton(
                modifier = Modifier.padding(Dimension.D500),
                icon = PodawanIcon.ArrowBack(""),
                onClick = onBackClicked
            )
        },
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = Dimension.D1000)
        ) {

            Text(
                text = "Sign up with an email and password",
                typography = PodawanTheme.typography.Heading.H1000
            )

            VerticalSpacerD1000()

            InputField(
                title = {},
                errorBorder = false,
                hint = "Email",
                enabled = !isLoading,
                fieldState = emailFieldState,
                onFieldUpdated = onEmailChanged,
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = Next,
                    keyboardType = Email
                )
            )

            VerticalSpacerD1000()

            InputField(
                title = {},
                errorBorder = true,
                enabled = !isLoading,
                hint = "Password",
                fieldState = passwordFieldState,
                onFieldUpdated = onPasswordChanged,
                focusRequester = passwordFocusRequester,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = Done,
                    keyboardType = Password
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        focusManager.clearFocus()
                        onSignupClicked()
                    },
                    enabled = isFormValid
                ) {
                    Text(text = "Sign up")
                }
            }

            VerticalSpacerD1000()
        }
    }
}

@Composable
@Preview
private fun PreviewScreen() {
    Preview {
        SignupScreen(
            emailFieldState = FieldState.Idle(""),
            passwordFieldState = FieldState.Idle(""),
            isFormValid = false,
            onEmailChanged = {},
            onPasswordChanged = {},
            onSignupClicked = {},
            isLoading = false,
            onBackClicked = {}
        )
    }
}