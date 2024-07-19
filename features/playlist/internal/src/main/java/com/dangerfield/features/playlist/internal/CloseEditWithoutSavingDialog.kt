package com.dangerfield.features.playlist.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import com.dangerfield.libraries.analytics.PageLogEffect
import com.dangerfield.libraries.analytics.PageType
import com.dangerfield.libraries.navigation.route
import com.dangerfield.ui.components.dialog.BasicDialog

@Composable
fun CloseEditWithoutSavingDialog(
    onDismiss: () -> Unit,
    onLeave: () -> Unit,
) {

    PageLogEffect(
        route = route("CloseEditWithoutSavingDialog"),
        type = PageType.Dialog,
    )

    BasicDialog(
        onDismissRequest = onDismiss,
        title = "Did you mean to leave?",
        description = "You have unsaved changes. Are you sure you want to leave?",
        primaryButtonText = "Leave",
        onPrimaryButtonClicked = onLeave,
        secondaryButtonText = "Cancel",
        onSecondaryButtonClicked = onDismiss
    )
}


@Composable
@Preview
private fun PreviewCloseEditWithoutSavingDialog() {
    com.dangerfield.libraries.ui.preview.Preview {
        CloseEditWithoutSavingDialog(
            onDismiss = {},
            onLeave = {},
        )
    }
}