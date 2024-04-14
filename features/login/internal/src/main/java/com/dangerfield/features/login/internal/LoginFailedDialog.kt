package com.dangerfield.features.login.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dangerfield.libraries.analytics.PageLogEffect
import com.dangerfield.libraries.analytics.PageType
import com.dangerfield.libraries.dictionary.dictionaryString
import com.dangerfield.libraries.navigation.route
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.components.dialog.BasicDialog
import com.dangerfield.oddoneoout.libraries.dictionary.R

@Composable
fun LoginFailedDialog(
    onDismiss: () -> Unit,
    errorClass: String?
) {

    PageLogEffect(
        route = loginErrorDialogRoute,
        type = PageType.Dialog,
        extras = bundleOf(
            "error_class" to (errorClass ?: "unknown")
        )
    )

    BasicDialog(
        onDismissRequest = onDismiss,
        title = "Oops!",
        description = "Something went wrong when we tried to log you in. Please check your internet connection and try again. If the problem persists, please contact support.",
        primaryButtonText = dictionaryString(R.string.app_okay_action),
        onPrimaryButtonClicked = onDismiss
    )
}


@Composable
@Preview
private fun PreviewJoinGameErrorDialogUnknown() {
    Preview {
        LoginFailedDialog(
            onDismiss = {},
            errorClass = null
        )
    }
}