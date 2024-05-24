package com.dangerfield.features.playlist.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.dangerfield.features.playlist.newPlaylistRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import se.ansman.dagger.auto.AutoBindIntoSet
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.dialog
import com.dangerfield.libraries.ui.showDeveloperMessage
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): GlobalNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {

        dialog(
            route = newPlaylistRoute.navRoute,
            arguments = newPlaylistRoute.navArguments
        ) {

            val viewModel = hiltViewModel<NewPlaylistViewModel>()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()
            val sheetState = rememberBottomSheetState()

            ObserveWithLifecycle(flow = viewModel.eventFlow) { event ->
                when (event) {
                    is NewPlaylistViewModel.Event.CreateFailed -> showDeveloperMessage { "Failed to make playlist" }
                    NewPlaylistViewModel.Event.PlaylistCreated -> {
                        router.dismissSheet(sheetState)
                    }
                }
            }

            NewPlaylistScreen(
                isLoading = state.isLoading,
                bottomSheetState = sheetState,
                playlistNameFieldState = state.playlistNameFieldState,
                onPlaylistNameChanged = {
                    viewModel.takeAction(
                        NewPlaylistViewModel.Action.UpdatePlaylistName(
                            it
                        )
                    )
                },
                onBackClicked = { router.dismissSheet(sheetState) },
                onCreateClicked = {
                    viewModel.takeAction(NewPlaylistViewModel.Action.Create)
                },
            )
        }
    }
}