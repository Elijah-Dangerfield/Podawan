package com.dangerfield.features.auth.internal.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.makeClickable
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.toPainter
import com.dangerfield.ui.components.CircularProgressIndicator
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.button.ButtonStyle
import com.dangerfield.ui.components.text.InputField
import com.dangerfield.ui.components.text.Text
import podawan.core.App

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    emailFieldState: FieldState<String>,
    passwordFieldState: FieldState<String>,
    isFormValid: Boolean,
    isLoading: Boolean,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onSignupClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = FocusRequester()
    val appConfiguration = LocalAppConfiguration.current

    Screen(modifier) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(horizontal = Dimension.D1000),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            VerticalSpacerD1000()

            Image(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
                    .clip(Radii.Card.shape)
                    .border(
                        2.dp,
                        PodawanTheme.colors.border.color,
                        Radii.Card.shape
                    ),
                painter = appConfiguration.image.toPainter(),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
            )

            VerticalSpacerD1000()


            Text(
                text = "Log in with your email and password",
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
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            focusManager.clearFocus()
                            onLoginClicked()
                        },
                        enabled = isFormValid
                    ) {
                        Text(text = "Login")
                    }

                    VerticalSpacerD1000()

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.NoBackground,
                        onClick = onSignupClicked,
                    ) {
                        Text(
                            text = "New to ${appConfiguration.appName}? Sign up".makeClickable(linkText = "Sign up"),
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            lineBreak = LineBreak(
                                strategy = LineBreak.Strategy.Balanced,
                                strictness = LineBreak.Strictness.Normal,
                                wordBreak = LineBreak.WordBreak.Default,
                            )
                        )
                    }
                    VerticalSpacerD1000()

                }
            }

            VerticalSpacerD1000()
        }
    }
}

@Composable
@Preview
private fun PreviewScreen() {
    com.dangerfield.libraries.ui.preview.Preview(app = App.TMG) {
        LoginScreen(
            emailFieldState = FieldState.Idle(""),
            passwordFieldState = FieldState.Idle(""),
            isFormValid = false,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            isLoading = false,
            onSignupClicked = {}
        )
    }
}

@Composable
@Preview
private fun PreviewScreenStuffYouShouldKnow() {
    com.dangerfield.libraries.ui.preview.Preview(app = App.StuffYouShouldKnow) {
        LoginScreen(
            emailFieldState = FieldState.Idle(""),
            passwordFieldState = FieldState.Idle(""),
            isFormValid = false,
            onEmailChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
            isLoading = false,
            onSignupClicked = {}
        )
    }
}