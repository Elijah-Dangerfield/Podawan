package com.dangerfield.features.playlist.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.playlist.addToPlaylistRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.bottomSheet
import com.dangerfield.libraries.ui.showMessage
import com.dangerfield.ui.components.FullScreenLoader
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheet
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class GlobalNavGraphBuilder @Inject constructor() : GlobalNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {

        bottomSheet(
            route = addToPlaylistRoute.navRoute,
            arguments = addToPlaylistRoute.navArguments
        ) {
            val sheetState = rememberBottomSheetState()
            val viewModel = hiltViewModel<AddToPlaylistViewModel>()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    is AddToPlaylistViewModel.Event.LoadFailed -> {
                        router.navigateToGeneralErrorDialog(errorClass = it::class.simpleName)
                    }
                    AddToPlaylistViewModel.Event.SaveFailed -> {
                        showMessage { "We're sorry we are unable to update the playlist" }
                    }
                    AddToPlaylistViewModel.Event.SaveSuccess -> {
                        showMessage { "Playlists updated" }
                        router.dismissSheet(sheetState)
                    }
                }
            }

            BottomSheet(
                showDragHandle = false,
                onDismissRequest = { router.dismissSheet(sheetState) }
            ) {
                if (state.isLoading) {
                    FullScreenLoader()
                } else {
                    AddToPlaylistScreen(
                        playlists = state.playlists,
                        onPlaylistChecked = {
                            viewModel.takeAction(AddToPlaylistViewModel.Action.UpdateChecked(it))
                        },
                        onSave = { viewModel.takeAction(AddToPlaylistViewModel.Action.Save) },
                        haveChangesBeenMade = state.haveChangesBeenMade
                    )
                }
            }
        }
    }
}
