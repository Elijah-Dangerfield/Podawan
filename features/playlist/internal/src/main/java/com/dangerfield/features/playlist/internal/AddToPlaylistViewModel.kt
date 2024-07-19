package com.dangerfield.features.playlist.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistRepository
import com.dangerfield.features.playlist.internal.AddToPlaylistViewModel.Action
import com.dangerfield.features.playlist.internal.AddToPlaylistViewModel.Event
import com.dangerfield.features.playlist.internal.AddToPlaylistViewModel.State
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.ui.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import podawan.core.Catching
import podawan.core.eitherWay
import javax.inject.Inject

@HiltViewModel
class AddToPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<State, Event, Action>(savedStateHandle) {

    private val episodeId = savedStateHandle.navArgument<String>(episodeIdArgument)

    override fun initialState(): State {
        return State()
    }

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.handleLoad()
            is Action.UpdateChecked -> action.handleCheckUpdate()
            is Action.Save -> action.handleSave()
        }
    }

    private suspend fun Action.Load.handleLoad() {
        if (episodeId == null) {
            sendEvent(Event.LoadFailed(NullPointerException("Episode ID is null")))
            return
        }
        playlistRepository.getPlaylists()
            .onSuccess { playlists ->
                val selectablePlaylists = playlists.map { playlist ->
                    Selectable(playlist, playlist.episodeIds.any { it == episodeId })
                }
                updateState {
                    it.copy(
                        playlists = selectablePlaylists.toPersistentList(),
                        isLoading = false
                    )
                }
            }
            .onFailure {
                sendEvent(Event.LoadFailed(it))
            }
    }

    private suspend fun Action.UpdateChecked.handleCheckUpdate() {
        updateState {
            it.copy(
                playlists = it.playlists.map { playlist ->
                    if (playlist.item.id == this.playlist.item.id) {
                        playlist.copy(isSelected = this.playlist.isSelected)
                    } else {
                        playlist
                    }
                }.toPersistentList(),
                haveChangesBeenMade = true
            )
        }
    }

    private suspend fun Action.Save.handleSave() {

        if (episodeId == null) {
            sendEvent(Event.SaveFailed)
            return
        }

        updateState { it.copy(isLoadingSave = true) }

        val additions = state.playlists.filter { it.isSelected }
        val removals = state.playlists.filter { !it.isSelected }

        val asyncAdditions = additions.map {
            viewModelScope.async {
                playlistRepository.addEpisodeToPlaylist(it.item.id, episodeId)
            }
        }

        val asyncRemovals = removals.map {
            viewModelScope.async {
                playlistRepository.removeEpisodeFromPlaylist(it.item.id, episodeId)
            }
        }

        val results = asyncAdditions + asyncRemovals

        Catching {
            results.awaitAll()
        }
            .onSuccess {
                sendEvent(Event.SaveSuccess)
            }.onFailure {
                sendEvent(Event.SaveFailed)
            }
            .eitherWay {
                updateState { it.copy(isLoadingSave = false) }
            }
    }


    data class State(
        val playlists: ImmutableList<Selectable<Playlist>> = persistentListOf(),
        val haveChangesBeenMade: Boolean = false,
        val isLoading: Boolean = true,
        val isLoadingSave: Boolean = false,
    )

    sealed class Event {
        data class LoadFailed(val e: Throwable) : Event()
        object SaveSuccess : Event()
        object SaveFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        data class UpdateChecked(val playlist: Selectable<Playlist>) : Action()

        object Save : Action()
    }
}
