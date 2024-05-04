package com.dangerfield.ui.components.dialog.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.color.ProvideContentColor
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.text.Text

@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    state: BottomSheetState = rememberBottomSheetState(),
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    val backgroundColor = PodawanTheme.colors.background
    val contentColor = PodawanTheme.colors.onBackground

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = state.materialSheetStateDelegate,
        windowInsets = BottomSheetDefaults.windowInsets,
        containerColor = backgroundColor.color,
        scrimColor = PodawanTheme.colors.backgroundOverlay.color,
        tonalElevation = 0.dp,
        dragHandle = {
            if (showDragHandle) {
                DragHandle(
                    modifier = Modifier.fillMaxWidth(0.2f),
                    color = contentColor.color
                )
            }
        },
    ) {
        ProvideContentColor(contentColor) {
            Column(
                modifier = modifier,
                horizontalAlignment = contentAlignment,
                content = content
            )
        }
    }
}

@Preview(heightDp = 500)
@Composable
private fun PreviewBottomSheet(
) {
    com.dangerfield.libraries.ui.preview.Preview {
        BottomSheet(
            onDismissRequest = {},
            state = rememberBottomSheetState(BottomSheetValue.Expanded),
        ) {
            Text(
                text = "Content",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = Dimension.D1400,
                        horizontal = Dimension.D400
                    ),
                textAlign = TextAlign.Center
            )
        }
    }
}



