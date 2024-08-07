package com.dangerfield.ui.components.dialog.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD800
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.dialog.ModalContent
import com.dangerfield.ui.components.text.Text

@Composable
fun BasicBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    state: BottomSheetState = rememberBottomSheetState(),
    showCloseButton: Boolean = false,
    showDragHandle: Boolean = true,
    backgroundColor: ColorResource = PodawanTheme.colors.background,
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    stickyTopContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    stickyBottomContent: @Composable (() -> Unit)? = null,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier,
        state = state,
        backgroundColor = backgroundColor,
        showDragHandle = showDragHandle,
        contentAlignment = contentAlignment,
    ) {
        Column(
            modifier = Modifier.padding(
                start = Dimension.D800,
                end = Dimension.D800,
                bottom = Dimension.D800
            )
        ) {
            if (showCloseButton) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        icon = PodawanIcon.Close("CLose"),
                        onClick = onDismissRequest
                    )
                }
            } else {
                VerticalSpacerD800()
            }

            ModalContent(
                modifier = modifier,
                topContent = stickyTopContent,
                content = content,
                backgroundColor = if(backgroundColor.color.alpha < 1f) backgroundColor.withAlpha(0f) else backgroundColor,
                bottomContent = stickyBottomContent,
            )
        }
    }
}

@Composable
@Preview
private fun PreviewBasicBottomSheetCloseButton() {
    com.dangerfield.libraries.ui.preview.Preview() {
        BasicBottomSheet(
            state = rememberBottomSheetState(BottomSheetValue.Expanded),
            onDismissRequest = { -> },
            modifier = Modifier,
            showCloseButton = true,
            stickyTopContent = { Text(text = "Top Content") },
            content = {
                Column {
                    Text(text = "content".repeat(100))
                    Text(text = "is good".repeat(100))
                }
            },
            stickyBottomContent = {
                Button(onClick = { }) {
                    Text(text = "Bottom Content")
                }
            },
        )
    }
}

@Composable
@Preview
private fun PreviewBasicBottomSheet() {
    com.dangerfield.libraries.ui.preview.Preview {
        BasicBottomSheet(
            state = rememberBottomSheetState(BottomSheetValue.Expanded),
            onDismissRequest = { -> },
            modifier = Modifier,
            stickyTopContent = { Text(text = "Top Content") },
            content = {
                Column {
                    Text(text = "content".repeat(10))
                    Text(text = "is good".repeat(10))
                }
            },
            stickyBottomContent = {
                Button(onClick = { }) {
                    Text(text = "Bottom Content")
                }
            },
        )
    }
}