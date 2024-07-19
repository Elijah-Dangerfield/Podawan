package com.dangerfield.features.playlist.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD1600
import com.dangerfield.libraries.ui.isValid
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheet
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetState
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetValue.Expanded
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.OutlineInputField
import com.dangerfield.ui.components.text.Text

@Composable
fun NewPlaylistScreen(
    isLoading: Boolean,
    playlistNameFieldState: FieldState<String>,
    onPlaylistNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    bottomSheetState: BottomSheetState = rememberBottomSheetState(initialState = Expanded),
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
) {

    BottomSheet(
        onDismissRequest = onBackClicked,
        showDragHandle = false,
        state = bottomSheetState
    ) {

        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Screen(modifier = modifier) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(top = Dimension.D800)
                    .padding(horizontal = Dimension.D800),
            ) {

                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        icon = PodawanIcon.Close(""),
                        onClick =  onBackClicked
                    )
                }

                VerticalSpacerD1600()

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "Give your playlist a name",
                        textAlign = TextAlign.Center
                    )

                    VerticalSpacerD1200()

                    OutlineInputField(
                        focusRequester = focusRequester,
                        title = {},
                        errorBorder = false,
                        hint = "My playlist #1",
                        enabled = !isLoading,
                        fieldState = playlistNameFieldState,
                        onFieldUpdated = onPlaylistNameChanged,
                        keyboardActions = KeyboardActions(
                            onAny = {
                                focusRequester.freeFocus()
                                focusManager.clearFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        )
                    )

                    VerticalSpacerD1200()

                    Button(
                        enabled = playlistNameFieldState.isValid(),
                        onClick = {
                        focusRequester.freeFocus()
                        focusManager.clearFocus()
                        if (playlistNameFieldState.isValid()) {
                            onCreateClicked()
                        }
                    }) {
                        Text(text = "Create")
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
            }
        }
    }
}


@Preview
@Composable
private fun PreviewNewPlaylistScreen() {
    Preview {
        NewPlaylistScreen(
            isLoading = false,
            playlistNameFieldState = FieldState.Idle(""),
            onPlaylistNameChanged = {},
            onBackClicked = {},
            onCreateClicked = {}
        )
    }
}