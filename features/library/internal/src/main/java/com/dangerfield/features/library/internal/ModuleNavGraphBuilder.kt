package com.dangerfield.features.library.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.playlist.navigateToNewPlaylist
import com.dangerfield.features.playlist.navigateToPlaylist
import com.dangerfield.libraries.navigation.LibraryTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.screen
import com.dangerfield.ui.components.FullScreenLoader
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : LibraryTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        screen(
            route = libraryRoute,
        ) {

            val viewModel: LibraryViewModel = hiltViewModel()

            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            when {
                state.isLoading -> FullScreenLoader()
                state.playlists.isEmpty() -> {
                    EmptyLibraryScreen(
                        onNewPlaylistClicked = { router.navigateToNewPlaylist() }
                    )
                }

                else -> {
                    LibraryScreen(
                        playlists = state.playlists,
                        onNewPlaylistClick = { router.navigateToNewPlaylist() },
                        onPlaylistClick = {
                            router.navigateToPlaylist(it.id)
                        }
                    )
                }
            }
        }
    }
}