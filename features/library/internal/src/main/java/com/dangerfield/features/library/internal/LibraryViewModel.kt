package com.dangerfield.features.library.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.library.internal.LibraryViewModel.Action
import com.dangerfield.features.library.internal.LibraryViewModel.Event
import com.dangerfield.features.library.internal.LibraryViewModel.State
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistRepository
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<State, Event, Action>(
    savedStateHandle
) {

    override fun initialState(): State = State()
    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.handleLoad()
            is Action.RefreshPlaylists -> action.handleRefreshPlaylists()
        }
    }

    init {
        takeAction(Action.Load)
    }

    private suspend fun Action.Load.handleLoad() {
        updateState { it.copy(isLoading = true) }
        val playlistFlow = playlistRepository.getPlaylistsFlow().getOrNull()

        if (playlistFlow == null) {
            updateState { it.copy(isLoading = false) }
            sendEvent(Event.LoadFailed)
            return
        }

        playlistFlow.collectIn(viewModelScope) { playlists ->
            updateState {
                it.copy(
                    isLoading = false,
                    playlists = playlists
                )
            }
        }
    }

    private suspend fun Action.RefreshPlaylists.handleRefreshPlaylists() {
        takeAction(Action.Load)
    }

    data class State(
        val playlists: List<Playlist> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Event {
        object LoadFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        object RefreshPlaylists : Action()
    }
}