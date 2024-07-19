package com.dangerfield.features.playlist.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.episodeDetails.registerEpisodeDetails
import com.dangerfield.features.playlist.editPlaylistRoute
import com.dangerfield.features.playlist.navigateToAddToPlaylist
import com.dangerfield.features.playlist.navigateToEditPlaylist
import com.dangerfield.features.playlist.newPlaylistRoute
import com.dangerfield.features.playlist.playlistIdArgument
import com.dangerfield.features.playlist.playlistRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.LibraryTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.dialog
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.navigation.screen
import com.dangerfield.libraries.ui.showDeveloperMessage
import com.dangerfield.libraries.ui.showMessage
import com.dangerfield.ui.components.FullScreenLoader
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import podawan.core.showDebugSnack
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : LibraryTabNavBuilder {

    @Suppress("LongMethod")
    override fun NavGraphBuilder.addDestinations(router: Router) {

        registerEpisodeDetails(
            router = router,
            route = playlistEpisodeDetailsRoute
        )

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
                    is NewPlaylistViewModel.Event.PlaylistCreated -> {
                        router.navigateToEditPlaylist(event.id, popOff = newPlaylistRoute)
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



        composable(
            route = playlistRoute.navRoute,
            arguments = playlistRoute.navArguments
        ) {

            val id = it.navArgument<Int>(playlistIdArgument) ?: return@composable

            val viewModel: PlaylistViewModel = hiltViewModel()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    PlaylistViewModel.Event.LoadFailed -> router.navigateToGeneralErrorDialog()
                    PlaylistViewModel.Event.PlaybackFailed -> showDebugSnack { "Playback failed" }
                }
            }

            if (state.isLoading) {
                FullScreenLoader()
            } else {
                PlaylistScreen(
                    name = state.name,
                    description = state.description,
                    heroImageUrl = state.heroImageUrl,
                    episodes = state.episodes,
                    currentlyPlayingEpisode = state.currentlyPlaying?.episode,
                    onEpisodePlayClicked = {
                        viewModel.takeAction(PlaylistViewModel.Action.PlayEpisode(it))
                    },
                    onEpisodePauseClicked = {
                        viewModel.takeAction(PlaylistViewModel.Action.PauseEpisode(it))
                    },
                    onEpisodeDownloadClicked = {
                        viewModel.takeAction(PlaylistViewModel.Action.DownloadEpisode(it))
                    },
                    onClickEpisode = {
                        router.toPlaylistEpisodeDetails(it.id)
                    },
                    onCurrentlyPlayingExitView = {
                        viewModel.takeAction(PlaylistViewModel.Action.CurrentlyPlayingNotShowing)
                    },
                    onCurrentlyPlayingEnterView = {
                        viewModel.takeAction(PlaylistViewModel.Action.CurrentlyPlayingShowing)
                    },
                    onAddToPlaylistClicked = { ep ->
                        router.navigateToAddToPlaylist(ep.id)
                    },
                    onNavigateBack = { router.goBack() },
                    onEditClicked = {
                        router.navigateToEditPlaylist(id)
                    }
                )
            }
        }

        screen(editPlaylistRoute) {
            val viewModel = hiltViewModel<EditPlaylistViewModel>()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()
            var showCloseDialog by remember { mutableStateOf(false) }

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    is EditPlaylistViewModel.Event.LoadFailed -> {
                        router.navigateToGeneralErrorDialog(errorClass = it::class.simpleName)
                    }

                    is EditPlaylistViewModel.Event.SaveSuccess -> {
                        router.goBack()
                    }

                    is EditPlaylistViewModel.Event.SaveFailed -> {
                        showMessage { "We're sorry we are unable to update the playlist" }
                    }
                }
            }

            Box {
                if (state.isLoading) {
                    FullScreenLoader()
                } else {
                    EditPlaylistScreen(
                        heroImageUrl = state.heroImageUrl,
                        nameFieldState = state.nameFieldState,
                        descriptionFieldState = state.descriptionFieldState,
                        episodes = state.episodes,
                        areChangesValid = state.isFormValid,
                        hasMadeChanges = state.hasMadeChanges,
                        updatePhoto = {
                            viewModel.takeAction(
                                EditPlaylistViewModel.Action.UpdateImageUri(
                                    it
                                )
                            )
                        },
                        onUpdateName = {
                            viewModel.takeAction(
                                EditPlaylistViewModel.Action.UpdateName(
                                    it
                                )
                            )
                        },
                        onUpdateDescription = {
                            viewModel.takeAction(
                                EditPlaylistViewModel.Action.UpdateDescription(
                                    it
                                )
                            )
                        },
                        onSave = { viewModel.takeAction(EditPlaylistViewModel.Action.Save) },
                        onClose = {
                            if (state.hasMadeChanges) {
                                showCloseDialog = true
                            } else {
                                router.goBack()
                            }
                        },
                    )
                }

                if (showCloseDialog) {
                    CloseEditWithoutSavingDialog(
                        onDismiss = { showCloseDialog = false },
                        onLeave = { router.goBack() },
                    )
                }
            }
        }
    }
}